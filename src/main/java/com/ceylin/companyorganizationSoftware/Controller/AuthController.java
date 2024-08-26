package com.ceylin.companyorganizationSoftware.Controller;


import com.ceylin.companyorganizationSoftware.Dto.Response.ActivationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Response.AuthenticationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Request.LoginRequest;
import com.ceylin.companyorganizationSoftware.Dto.Request.ActivateRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.Response;
import com.ceylin.companyorganizationSoftware.Dto.Request.SetPasswordRequest;
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
    public ResponseEntity<Response<Object>> activate(@RequestBody ActivateRequest activateRequest) {

        return authService.activate(activateRequest.getEmail());
    }

    @PostMapping ("/set-password/{token}")
    public String setPassword(@PathVariable("token") String token,@RequestBody SetPasswordRequest setPasswordRequest) throws Exception {

        return authService.setPassword(token,setPasswordRequest.getPassword());
    }

}
