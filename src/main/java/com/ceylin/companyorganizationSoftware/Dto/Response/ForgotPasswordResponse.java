package com.ceylin.companyorganizationSoftware.Dto.Response;


import org.springframework.http.ResponseEntity;
import lombok.Data;

@Data
public class ForgotPasswordResponse<T> {
  private String message;
  private String token;

  private ForgotPasswordResponse(String message, String data) {
    this.message = message;
    this.token = data;
  }
  public static <T> ResponseEntity<ForgotPasswordResponse<Object>> ok(String message, String token) {
    return ResponseEntity.ok(new ForgotPasswordResponse<>(message, token));
  }

  public static <T> ResponseEntity<ForgotPasswordResponse<Object>> badRequest(String message) {
    return ResponseEntity.badRequest().body(new ForgotPasswordResponse<>(message, null));
  }

  public static <T> ResponseEntity<ForgotPasswordResponse<Object>> notFound(String message) {
    return ResponseEntity.status(404).body(new ForgotPasswordResponse<>(message, null));
  }

  public static <T> ResponseEntity<ForgotPasswordResponse<Object>> unauthorized(String message) {
    return ResponseEntity.status(401).body(new ForgotPasswordResponse<>(message, null));
  }
}
