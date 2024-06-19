package com.debargha.NIC.Document_Management_Framework.Payload;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class initRequest {
    private String client_id;
    private String client_secret;
}
