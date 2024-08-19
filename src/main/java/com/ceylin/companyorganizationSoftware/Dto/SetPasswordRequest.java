package com.ceylin.companyorganizationSoftware.Dto;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetPasswordRequest {
  private String token;
  private String password;
}
