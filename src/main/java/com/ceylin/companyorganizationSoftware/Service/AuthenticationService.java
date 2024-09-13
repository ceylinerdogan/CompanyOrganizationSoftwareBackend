package com.ceylin.companyorganizationSoftware.Service;

import com.ceylin.companyorganizationSoftware.Config.JwtService;

import com.ceylin.companyorganizationSoftware.Dto.Request.*;
import com.ceylin.companyorganizationSoftware.Dto.Response.ActivationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Response.AuthenticationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Response.Response;
import com.ceylin.companyorganizationSoftware.Model.User;
import com.ceylin.companyorganizationSoftware.Model.UserRole;
import com.ceylin.companyorganizationSoftware.Repository.UserRepository;
import com.ceylin.companyorganizationSoftware.token.ConfirmationToken;
import com.ceylin.companyorganizationSoftware.token.ConfirmationTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailService emailService;
  private final ConfirmationTokenRepository confirmationTokenRepository;

  public AuthenticationResponse login(LoginRequest loginRequest){
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            )
    );
    var user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(()-> new IllegalArgumentException(" Invalid email or password!"));


    var jwt = jwtService.generateToken(user);
    return AuthenticationResponse.builder().token(jwt).build();
  }

    public ResponseEntity<ActivationResponse<Object>> activate(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        if (user.getPassword() != null || user.isEnabled()) {
            return ActivationResponse.badRequest("User already activated.");
        }

        // Generate a confirmation token for the user
        ConfirmationToken token = new ConfirmationToken(user);
        confirmationTokenRepository.save(token);

        // Send the activation email without the token in the link
        String activationLink = "https://company-organization-software-coral.vercel.app/setPassword";
        emailService.sendEmail(user.getEmail(), "Activate your account",
                "Click the link to set your password: " + activationLink);

        // Return the token in the response, frontend can store it and use it later
        return ActivationResponse.ok("Activation mail sent successfully", token.getToken());
    }
  public HttpStatus setPassword(String token,String password) {

      System.out.println("Received token: " + token);

   ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token!"));

    System.out.println("confirmation token: " + confirmationToken.getId());

    if (!(confirmationToken.getExpiryDate().before(new Date()))) {
        if(PasswordValidator.isValid(password)){
            User user = confirmationToken.getUser();

            user.setPassword(passwordEncoder.encode(password));
            user.setIsEnabled(true);
            userRepository.save(user);
            confirmationTokenRepository.delete(confirmationToken);

            return HttpStatus.OK;
        }
        else{
            return  HttpStatus.BAD_REQUEST;
        }
    }
    else{
        return HttpStatus.BAD_REQUEST;
    }

  }

    public ResponseEntity<Response<Object>> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        var user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        // Check if the user is active
        if (user.isEnabled()) {
            // If the user is active, proceed with generating a reset token
            ConfirmationToken token = new ConfirmationToken(user);
            confirmationTokenRepository.save(token);

            // Send reset link without the token in the URL
            String resetLink = "https://company-organization-software-coral.vercel.app/setNewPassword";
            emailService.sendEmail(user.getEmail(), "Reset your password",
                    "Click the link to reset your password: " + resetLink);

            // Return the token in the response, frontend can store it and use it later
            return Response.ok("Password reset link sent successfully", token.getToken());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>("User is not active. Cannot reset password", null));
        }
    }

    // Handle Password Reset using the token from request body
    public HttpStatus handlePasswordReset(String token, String newPassword) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token!"));

        if (!(confirmationToken.getExpiryDate().before(new Date()))) {
            if (PasswordValidator.isValid(newPassword)) {
                User user = confirmationToken.getUser();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                confirmationTokenRepository.delete(confirmationToken);

                return HttpStatus.OK;
            } else {
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }


}
