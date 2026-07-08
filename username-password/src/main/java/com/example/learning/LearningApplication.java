package com.example.learning;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learning.model.entity.MyUser;
import com.example.learning.repository.MyUserRepo;

@SpringBootApplication
public class LearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(MyUserRepo myUserRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            MyUser newUser = new MyUser(null, "nhannk15", passwordEncoder.encode("21012006"), "USER");
            myUserRepo.save(newUser);
        };
    }

}
