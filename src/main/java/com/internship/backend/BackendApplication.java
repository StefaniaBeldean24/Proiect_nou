package com.internship.backend;

import com.internship.backend.model.Users;
import com.internship.backend.repository.UserRepository;
import com.internship.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


	/*@Bean
	public CommandLineRunner run(UserRepository userRepository) throws Exception {
		return (String[] args) ->
	}*/


}
