package com.ceylin.companyorganizationSoftware.Dto.Response;


import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
public class Response<T> {
  private String message;
  private T data;

  private Response(String message, T data) {
    this.message = message;
    this.data = data;
  }
  public static <T> ResponseEntity<Response<T>> ok(String message, T data) {
    return ResponseEntity.ok(new Response<>(message, data));
  }

  public static <T> ResponseEntity<Response<T>> badRequest(String message) {
    return ResponseEntity.badRequest().body(new Response<>(message, null));
  }

  public static <T> ResponseEntity<Response<T>> notFound(String message) {
    return ResponseEntity.status(404).body(new Response<>(message, null));
  }

  public static <T> ResponseEntity<Response<T>> unauthorized(String message) {
    return ResponseEntity.status(401).body(new Response<>(message, null));
  }
}
