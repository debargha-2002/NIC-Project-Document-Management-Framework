package com.debargha.NIC.Document_Management_Framework.Model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Watermark {

    private long application_transaction_id;
    private String watermark;
}
