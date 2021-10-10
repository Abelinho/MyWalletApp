package com.abel.wallet.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abel.wallet.api.entities.User;
import com.abel.wallet.api.entities.Wallet;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Long> {
 Wallet findByWalletNumber(String walletNumber);
 
 Wallet findByUser(User user);
}
