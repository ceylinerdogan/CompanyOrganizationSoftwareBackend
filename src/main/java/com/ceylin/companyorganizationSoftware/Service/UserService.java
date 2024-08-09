package com.ceylin.companyorganizationSoftware.Service;

import com.ceylin.companyorganizationSoftware.Model.User;
import com.ceylin.companyorganizationSoftware.Repository.UserRepository;
import com.ceylin.companyorganizationSoftware.Model.PasswordResetToken;
import com.ceylin.companyorganizationSoftware.Repository.PasswordResetTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Lazy
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

//    public User saveUser(User user) {
//        user.setPassword(passwordEncoder().encode(user.getPassword()));
//        user.setCreatedAt(LocalDateTime.now());
//        return userRepository.save(user);
//    }



//    public void registerUser(String email) {
//        if (userRepository.findByEmail(email).isPresent()) {
//            throw new IllegalArgumentException("Email is already taken");
//        }
//
//        User user = new User();
//        user.setEmail(email);
//        user.setEnabled(false);
//        user.setActive(true);
//        user.setCreatedAt(LocalDateTime.now());
//        userRepository.save(user);
//
//        // Generate token
//        String token = UUID.randomUUID().toString();
//        PasswordResetToken passwordResetToken = new PasswordResetToken();
//        passwordResetToken.setToken(token);
//        passwordResetToken.setEmail(email);
//        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
//        tokenRepository.save(passwordResetToken);
//
//        // Send email
//        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
//        emailService.sendEmail(email, "Password Reset Request", "Click the link to set your password: " + resetUrl);
//    }
//    public void setPassword(String token, String password) {
//        PasswordResetToken resetToken = tokenRepository.findByToken(token)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
//
//        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("Token has expired");
//        }
//
//        User user = userRepository.findByEmail(resetToken.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        user.setPassword(passwordEncoder().encode(password));
//        user.setEnabled(true);
//        userRepository.save(user);
//
//        // Delete the token after use
//        tokenRepository.delete(resetToken);
//    }
    public String authenticateUser(String email, String password) {
//        AuthenticationConfiguration authenticationConfiguration;
//        Authentication authentication =  authenticationManager().authtentication(
//                new UsernamePasswordAuthenticationToken(email, password)
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "User authenticated successfully";
    }

}