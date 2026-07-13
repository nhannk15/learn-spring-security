package com.example.authorization;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.authorization.model.entity.MyUser;
import com.example.authorization.repository.MyUserRepository;

@SpringBootApplication
public class AuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(MyUserRepository myUserRepository, PasswordEncoder encoder) {
        return args -> {
            MyUser user1 = new MyUser(null, "nhannk15", encoder.encode("21012006"), "ADMIN");
            MyUser user2 = new MyUser(null, "nhannk21", encoder.encode("21012006"), "USER");
            myUserRepository.save(user1);
            myUserRepository.save(user2);
        };
    }

}
