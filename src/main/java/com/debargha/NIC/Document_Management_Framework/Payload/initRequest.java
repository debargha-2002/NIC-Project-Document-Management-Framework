package com.debargha.NIC.Document_Management_Framework.Payload;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class initRequest {
    private String clientId;
    private String clientSecret;
}
