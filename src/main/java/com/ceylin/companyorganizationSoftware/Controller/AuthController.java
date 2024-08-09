package com.ceylin.companyorganizationSoftware.Controller;

import com.ceylin.companyorganizationSoftware.Dto.LoginRequest;
import com.ceylin.companyorganizationSoftware.Dto.RegisterRequest;
import com.ceylin.companyorganizationSoftware.Service.AuthenticationService;
import com.ceylin.companyorganizationSoftware.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));

    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));

    }


}
