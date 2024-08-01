package com.ceylin.companyorganizationSoftware.Controller;

import com.ceylin.companyorganizationSoftware.Dto.LoginRequest;
import com.ceylin.companyorganizationSoftware.Dto.RegisterRequest;
import com.ceylin.companyorganizationSoftware.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String message = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest.getEmail());
        return ResponseEntity.ok("Registration email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        userService.setPassword(token, password);
        return ResponseEntity.ok("Password has been reset");
    }


}
