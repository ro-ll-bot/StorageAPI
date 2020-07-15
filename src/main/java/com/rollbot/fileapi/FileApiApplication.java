package com.rollbot.fileapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com")
public class FileApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileApiApplication.class, args);
    }

}
