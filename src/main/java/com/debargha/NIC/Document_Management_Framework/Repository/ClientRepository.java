package com.debargha.NIC.Document_Management_Framework.Repository;

import com.debargha.NIC.Document_Management_Framework.Model.Client;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {

    Optional<Client> findByClientId(String clientId);
    Optional<Client> findByClientIdAndClientSecret(String clientId, String clientSecret);
}
