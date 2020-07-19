package com.example.demo.loginsocial.config;

import com.example.demo.loginsocial.service.GoogleConnectionSignup;
import com.example.demo.loginsocial.service.GoogleSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.google.connect.GoogleConnectionFactory;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "com.example.demo.loginsocial.service" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.social.google.appSecret}")
	String appSecret;

	@Value("${spring.social.google.appId}")
	String appId;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private GoogleConnectionSignup googleConnectionSignup;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/login*","/signin/**","/signup/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.and()
			.logout();
	} // @formatter:on

	@Bean
	// @Primary
	public ProviderSignInController providerSignInController() {
		ConnectionFactoryLocator connectionFactoryLocator = connectionFactoryLocator();
		UsersConnectionRepository usersConnectionRepository = getUsersConnectionRepository(connectionFactoryLocator);
		((InMemoryUsersConnectionRepository) usersConnectionRepository).setConnectionSignUp(googleConnectionSignup);
		return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new GoogleSignInAdapter());
	}

	private ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new GoogleConnectionFactory(appId, appSecret));
		return registry;
	}

	private UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		return new InMemoryUsersConnectionRepository(connectionFactoryLocator);
	}
}