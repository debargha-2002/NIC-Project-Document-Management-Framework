package com.debargha.NIC.Document_Management_Framework.Model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PDFPassword{
    private long application_transaction_id;
    private String password;
}
