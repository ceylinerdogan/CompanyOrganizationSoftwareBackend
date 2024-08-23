package com.ceylin.companyorganizationSoftware.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String role;
  private String department;
  private String company;
}
