package com.example.demo.loginsocial.persistence.repository;

import com.example.demo.loginsocial.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(final String username);

}