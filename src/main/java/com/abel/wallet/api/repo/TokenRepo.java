package com.abel.wallet.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abel.wallet.api.entities.Token;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
	
	Token findByEmailAddressAndToken(String emailAddress, String token);
	
}
