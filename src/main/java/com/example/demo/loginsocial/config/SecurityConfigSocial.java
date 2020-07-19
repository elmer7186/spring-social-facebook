package com.example.demo.loginsocial.config;

import com.example.demo.loginsocial.service.GoogleConnectionSignup;
import com.example.demo.loginsocial.service.GoogleSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.google.connect.GoogleConnectionFactory;

public class SecurityConfigSocial extends WebSecurityConfigurerAdapter {

	@Autowired
	private GoogleConnectionSignup googleConnectionSignup;

	@Value("${spring.social.google.appSecret}")
	String appSecret;

	@Value("${spring.social.google.appId}")
	String appId;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/login*","/signin/**","/signup/**").permitAll();
	}

	@Bean
	public ProviderSignInController providerSignInController() {
		ConnectionFactoryLocator connectionFactoryLocator =
			connectionFactoryLocator();
		UsersConnectionRepository usersConnectionRepository =
			getUsersConnectionRepository(connectionFactoryLocator);
		((InMemoryUsersConnectionRepository) usersConnectionRepository)
			.setConnectionSignUp(googleConnectionSignup);
		return new ProviderSignInController(connectionFactoryLocator,
			usersConnectionRepository, new GoogleSignInAdapter());
	}

	private ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new GoogleConnectionFactory(appId, appSecret));
		return registry;
	}

	private UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator
		connectionFactoryLocator) {
		return new InMemoryUsersConnectionRepository(connectionFactoryLocator);
	}
}
