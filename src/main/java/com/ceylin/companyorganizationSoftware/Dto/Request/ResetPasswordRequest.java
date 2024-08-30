package com.ceylin.companyorganizationSoftware.Dto.Request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest {
  private String email;
}
