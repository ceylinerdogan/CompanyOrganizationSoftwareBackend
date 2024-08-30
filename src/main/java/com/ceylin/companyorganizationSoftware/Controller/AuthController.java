package com.ceylin.companyorganizationSoftware.Controller;


import com.ceylin.companyorganizationSoftware.Dto.Request.ResetPasswordRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.ActivationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Response.AuthenticationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Request.LoginRequest;
import com.ceylin.companyorganizationSoftware.Dto.Request.ActivateRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.Response;
import com.ceylin.companyorganizationSoftware.Dto.Request.SetPasswordRequest;
import com.ceylin.companyorganizationSoftware.Model.User;
import com.ceylin.companyorganizationSoftware.Repository.UserRepository;
import com.ceylin.companyorganizationSoftware.Service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {
    private final AuthenticationService authService;

    private final UserRepository userRepository;

    @Operation(summary = "Login", description = "Authenticate the user and return a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "Activate User", description = "Activate a user account using their email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activation email sent"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/activate-user")
    public ResponseEntity<Response<Object>> activate(@RequestBody ActivateRequest activateRequest) {

        return authService.activate(activateRequest.getEmail());
    }
    @Operation(summary = "Set Password", description = "Set a new password for a user using a token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password set successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid token or password")
    })
    @PostMapping ("/set-password/{token}")
    public HttpStatus setPassword(@PathVariable("token") String token, @RequestBody SetPasswordRequest setPasswordRequest) throws Exception {

        return authService.setPassword(token,setPasswordRequest.getPassword());
    }
    @Operation(summary = "Reset Password", description = "Request a password reset for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset request successful"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Response<Object>> resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return authService.resetPassword(resetPasswordRequest);
    }
    @Operation(summary = "Handle Password Reset", description = "Set a new password using the reset token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid token or password")
    })
    @PostMapping("/reset-password/{token}")
    public HttpStatus handlePasswordReset(
            @PathVariable("token") String token,
            @RequestBody SetPasswordRequest setPasswordRequest) {
        return authService.handlePasswordReset(token, setPasswordRequest.getPassword());
    }

    @GetMapping ("/getAllUsers")
    public List<User> getAllUser() throws Exception {

        return userRepository.findAll();
    }

}
