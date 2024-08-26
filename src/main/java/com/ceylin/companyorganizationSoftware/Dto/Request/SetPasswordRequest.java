package com.ceylin.companyorganizationSoftware.Dto.Request;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetPasswordRequest {
  private String password;
}
