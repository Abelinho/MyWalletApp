package com.abel.wallet.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abel.wallet.api.entities.Transactions;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions, Long> {

}
