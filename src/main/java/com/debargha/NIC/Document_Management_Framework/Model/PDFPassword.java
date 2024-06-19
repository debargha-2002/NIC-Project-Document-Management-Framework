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

//    // Constructors (if needed)
//    public PDFPassword() {
//    }

//    public PDFPassword(long application_transaction_id, String password) {
//        this.application_transaction_id = application_transaction_id;
//        this.password = password;
//    }

    // Getters and setters
//    public long getapplication_transaction_id() {
//        return application_transaction_id;
//    }
//
//    public void setapplication_transaction_id(long application_transaction_id) {
//        this.application_transaction_id = application_transaction_id;
//    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
