package com.ceylin.companyorganizationSoftware.Controller;


import com.ceylin.companyorganizationSoftware.Dto.Request.ResetPasswordRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.ActivationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Response.AuthenticationResponse;
import com.ceylin.companyorganizationSoftware.Dto.Request.LoginRequest;
import com.ceylin.companyorganizationSoftware.Dto.Request.ActivateRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.ForgotPasswordResponse;
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
    public ResponseEntity<ActivationResponse<Object>> activate(@RequestBody ActivateRequest activateRequest) {

        return authService.activate(activateRequest.getEmail());
    }
    @Operation(summary = "Set Password", description = "Set a new password for a user using a token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password set successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid token or password")
    })
    @PostMapping ("/set-password")
    public ResponseEntity<Object> setPassword(@RequestBody SetPasswordRequest setPasswordRequest) throws Exception {
        // Now, we are getting the token from the request body.
        String token = setPasswordRequest.getToken();
        String password = setPasswordRequest.getPassword();

        HttpStatus status = authService.setPassword(token, password);
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok("Password set successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token or password.");
        }
    }
    @Operation(summary = "Request Password Reset", description = "Request a password reset for a user via sending email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset request successful"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse<Object>> resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return authService.resetPassword(resetPasswordRequest);
    }

    @Operation(summary = "Handle Password Reset", description = "Set a new password using the reset token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid token or password")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Object> handlePasswordReset(@RequestBody SetPasswordRequest setPasswordRequest) {
        String token = setPasswordRequest.getToken();
        String newPassword = setPasswordRequest.getPassword();

        HttpStatus status = authService.handlePasswordReset(token, newPassword);
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token or password.");
        }
    }


    @GetMapping ("/getAllUsers")
    public List<User> getAllUser() throws Exception {

        return userRepository.findAll();
    }

}
