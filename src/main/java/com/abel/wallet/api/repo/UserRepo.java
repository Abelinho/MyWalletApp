package com.abel.wallet.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abel.wallet.api.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	public User findByEmailAddress(String emailAddress);
}
