package com.debargha.NIC.Document_Management_Framework.Repository;




import com.debargha.NIC.Document_Management_Framework.Model.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends MongoRepository<ClientDocument, UUID> {

    @Query("{ 'created_for.person_id': ?0 }")
    List<ClientDocument> findByPersonId(int person_id);
    @Query("{ 'created_for.mobile_number': ?0 }")
    List<ClientDocument> findByMobileNumber(long mobile_number);

   // List<ClientDocument> findByDocument_id(UUID document_id);
    @Query("{'file_information.application_transaction_id' :  ?0}")
    Optional<ClientDocument> findByApplication_transaction_id(long application_transaction_id);

}
