package com.ceylin.companyorganizationSoftware.Service;

import com.ceylin.companyorganizationSoftware.Config.JwtService;
import com.ceylin.companyorganizationSoftware.Controller.AuthenticationResponse;
import com.ceylin.companyorganizationSoftware.Dto.LoginRequest;
import com.ceylin.companyorganizationSoftware.Repository.UserRepository;
import com.ceylin.companyorganizationSoftware.token.ConfirmationToken;
import com.ceylin.companyorganizationSoftware.token.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

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

  public String activate(String email){
    var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found!"));

    if (user.getPassword() != null || user.isEnabled()) {
      throw new IllegalStateException("User already activated.");
    }
    ConfirmationToken token = new ConfirmationToken(user);
    confirmationTokenRepository.save(token);

    String activationLink = "http://localhost:8080/api/auth/set-password?token=" + token.getToken();
    emailService.sendEmail(user.getEmail(), "Activate your account",
            "Click the link to set your password: " + activationLink);

    return "Activation email sent.";
  }
  public String setPassword(String token, String password) {
    var confirmationToken = confirmationTokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token!"));

    if (confirmationToken.getExpiryDate().before(new Date())) {
      throw new IllegalArgumentException("Token expired.");
    }

    var user = confirmationToken.getUser();
    user.setPassword(passwordEncoder.encode(password));
    user.setIsEnabled(true);
    userRepository.save(user);
    confirmationTokenRepository.delete(confirmationToken);

    return "Account activated successfully.";
  }

}
