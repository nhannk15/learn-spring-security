package com.example.authen_authou_exception;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.authen_authou_exception.model.entity.MyUser;
import com.example.authen_authou_exception.repository.MyUserRepository;

@SpringBootApplication
public class AuthenAuthouExceptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenAuthouExceptionApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            MyUser myUser1 = new MyUser(null, "nhannk15", passwordEncoder.encode("21012006"), "ADMIN", null);
            MyUser myUser2 = new MyUser(null, "nhannk21", passwordEncoder.encode("21012006"), "USER", null);
            myUserRepository.save(myUser1);
            myUserRepository.save(myUser2);
        };
    }

}
