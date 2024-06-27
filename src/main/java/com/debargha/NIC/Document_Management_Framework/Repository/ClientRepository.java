package com.debargha.NIC.Document_Management_Framework.Repository;

import com.debargha.NIC.Document_Management_Framework.Model.Client;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {

    @Query("{ 'client_id' : ?0 }")
    Optional<Client> findByClient_id(String client_id);
    @Query("{'client_id': ?0,'client_secret': ?1}")
    Optional<Client> findByClient_idAndClient_secret(String client_id, String client_secret);
    @Query("{'mobile_no':  ?0}")
    Optional<Client> findByMobile_no(long mobile_no);
}
