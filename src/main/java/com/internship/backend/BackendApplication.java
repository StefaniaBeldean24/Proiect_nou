package com.internship.backend;

import com.internship.backend.model.Authority;
import com.internship.backend.model.Users;
import com.internship.backend.repository.AuthorityRepository;
import com.internship.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCouchbaseRepositories(basePackages = "com.internship.backend.repository")
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


}
