package com.debargha.NIC.Document_Management_Framework.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class initResponse {

    private String token;
    private String message;

}
