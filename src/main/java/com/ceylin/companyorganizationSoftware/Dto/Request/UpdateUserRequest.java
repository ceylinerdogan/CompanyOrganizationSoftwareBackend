package com.ceylin.companyorganizationSoftware.Dto.Request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
  private String firstName;
  private String lastName;
  private String email;
  private String company;
  private String department;
  private String role;
}
