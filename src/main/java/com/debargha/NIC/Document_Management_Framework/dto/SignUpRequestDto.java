package com.debargha.NIC.Document_Management_Framework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private long mobile_No;
    private String email_Id;
    private String name;
    private String gender;
    private String dob;
    private String address;
}
