package com.example.vrcSecurityAssignment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDto {
    private String name;
    private String email;
    private String token;
    private String role;
}
