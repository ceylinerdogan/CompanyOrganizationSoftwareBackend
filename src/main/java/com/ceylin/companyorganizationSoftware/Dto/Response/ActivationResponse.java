package com.ceylin.companyorganizationSoftware.Dto.Response;


import org.springframework.http.ResponseEntity;
import lombok.Data;

@Data
public class ActivationResponse<T> {
  private String message;
  private String token;

  private ActivationResponse(String message, String data) {
    this.message = message;
    this.token = data;
  }
  public static <T> ResponseEntity<ActivationResponse<T>> ok(String message, String token) {
    return ResponseEntity.ok(new ActivationResponse<>(message, token));
  }

  public static <T> ResponseEntity<ActivationResponse<Object>> badRequest(String message) {
    return ResponseEntity.badRequest().body(new ActivationResponse<>(message, null));
  }

  public static <T> ResponseEntity<ActivationResponse<Object>> notFound(String message) {
    return ResponseEntity.status(404).body(new ActivationResponse<>(message, null));
  }

  public static <T> ResponseEntity<ActivationResponse<Object>> unauthorized(String message) {
    return ResponseEntity.status(401).body(new ActivationResponse<>(message, null));
  }
}
