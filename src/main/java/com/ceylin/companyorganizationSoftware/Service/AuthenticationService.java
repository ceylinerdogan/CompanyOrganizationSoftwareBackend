package com.ceylin.companyorganizationSoftware.Service;

import com.ceylin.companyorganizationSoftware.Config.JwtService;

import com.ceylin.companyorganizationSoftware.Dto.Request.ActivateRequest;
import com.ceylin.companyorganizationSoftware.Dto.Request.AddUserRequest;
import com.ceylin.companyorganizationSoftware.Dto.Request.SetPasswordRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.ActivationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Response.AuthenticationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Request.LoginRequest;
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

  public  ResponseEntity<Response<Object>> activate(String email){
    var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found!"));

    if (user.getPassword() != null || user.isEnabled()) {
      throw new IllegalStateException("User already activated.");
    }
    ConfirmationToken token = new ConfirmationToken(user);
    confirmationTokenRepository.save(token);
    System.out.println("Generated token: " + token.getToken());

    String activationLink = "http://localhost:8080/api/auth/set-password/"+token.getToken();
    emailService.sendEmail(user.getEmail(), "Activate your account",
            "Click the link to set your password: " + activationLink);


    return Response.ok("Activation Mail sent successfully",null);
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

}
