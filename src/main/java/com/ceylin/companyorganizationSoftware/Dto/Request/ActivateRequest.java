package com.ceylin.companyorganizationSoftware.Dto.Request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRequest {
    private String email;
}
