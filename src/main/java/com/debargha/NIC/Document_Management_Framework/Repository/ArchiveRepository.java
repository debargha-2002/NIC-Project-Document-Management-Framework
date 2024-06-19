package com.debargha.NIC.Document_Management_Framework.Repository;

import com.debargha.NIC.Document_Management_Framework.Model.ArchiveDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ArchiveRepository extends MongoRepository<ArchiveDocument, Long> {
    @Query("{'application_transaction_id' :  ?0 }")
    Optional<ArchiveDocument> findByApplication_transaction_id(Long application_transaction_id);
}
