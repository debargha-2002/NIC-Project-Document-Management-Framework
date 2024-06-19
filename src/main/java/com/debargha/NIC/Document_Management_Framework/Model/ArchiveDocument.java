package com.debargha.NIC.Document_Management_Framework.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "archive_documents")
public class ArchiveDocument {

    private long application_transaction_id;
    private String archive_comments;
}
