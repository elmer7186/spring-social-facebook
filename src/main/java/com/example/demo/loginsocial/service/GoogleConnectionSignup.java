package com.example.demo.loginsocial.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import com.example.demo.loginsocial.persistence.model.User;
import com.example.demo.loginsocial.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

@Service
public class GoogleConnectionSignup implements ConnectionSignUp {

	@Autowired
	private UserRepository userRepository;

	@Override
	public String execute(Connection<?> connection) {
		User user = new User();
		user.setUsername(connection.getDisplayName());
		user.setPassword(randomAlphabetic(8));
		userRepository.save(user);
		return user.getUsername();
	}
}
