package com.debargha.NIC.Document_Management_Framework.Model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "reviews")
public class Review {

    private Long application_transaction_id;
    private String review;
}
