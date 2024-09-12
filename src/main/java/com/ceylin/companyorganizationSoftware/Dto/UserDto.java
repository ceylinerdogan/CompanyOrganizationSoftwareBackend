package com.ceylin.companyorganizationSoftware.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String role;
  private String department;
  private String company;
  private String profilePicture;
}
