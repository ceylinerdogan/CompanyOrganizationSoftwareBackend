package com.ceylin.companyorganizationSoftware.Service;

import com.ceylin.companyorganizationSoftware.Config.JwtService;
import com.ceylin.companyorganizationSoftware.Controller.AuthenticationResponse;
import com.ceylin.companyorganizationSoftware.Dto.LoginRequest;
import com.ceylin.companyorganizationSoftware.Dto.RegisterRequest;
import com.ceylin.companyorganizationSoftware.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse login(LoginRequest loginRequest){
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            )
    );
    var user = repository.findByEmail(loginRequest.getEmail())
            .orElseThrow(()-> new IllegalArgumentException(" Invalid email or password!"));


    var jwt = jwtService.generateToken((UserDetails) user);
    return AuthenticationResponse.builder().token(jwt).build();
  }

  public AuthenticationResponse register(RegisterRequest registerRequest){
    var user = UserDetails.builder().
            .firstName(registerRequest.getFirstName())
            .lastname(registerRequest.getLastName())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .build();
    repository.save(user);
    var jwtToken= jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }

}
