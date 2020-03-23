package com.example.demobd.repository;

import com.example.demobd.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepositoty extends MongoRepository<Contract, String> {
}
