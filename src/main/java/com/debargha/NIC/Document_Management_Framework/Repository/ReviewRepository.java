package com.debargha.NIC.Document_Management_Framework.Repository;

import com.debargha.NIC.Document_Management_Framework.Model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, Long> {
    @Query("{'application_transaction_id': ?0}")
    Optional<Review> findByApplication_transaction_id(Long application_transaction_id);


}
