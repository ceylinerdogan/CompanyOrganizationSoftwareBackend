package com.ceylin.companyorganizationSoftware.Controller;

import com.ceylin.companyorganizationSoftware.Dto.LoginRequest;
import com.ceylin.companyorganizationSoftware.Dto.ActivateRequest;
import com.ceylin.companyorganizationSoftware.Dto.SetPasswordRequest;
import com.ceylin.companyorganizationSoftware.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/activate-user")
    public String activate(@RequestBody ActivateRequest activateRequest) throws Exception {

        return authService.activate(activateRequest.getEmail());
    }

    @PostMapping("/set-password")
    public String setPassword(@RequestBody SetPasswordRequest setPasswordRequest) throws Exception {

        return authService.setPassword(setPasswordRequest.getToken(), setPasswordRequest.getPassword());
    }

}
