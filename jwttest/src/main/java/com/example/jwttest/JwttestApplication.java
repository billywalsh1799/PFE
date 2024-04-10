package com.example.jwttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JwttestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwttestApplication.class, args);
	}
	/* @Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
		
			userService.saveUser(new User(null, "John Travolta", "john", "Actor"));
			userService.saveUser(new User(null, "Jim Carry", "1234", "Comedian"));

			
		};
	} */
	

}
