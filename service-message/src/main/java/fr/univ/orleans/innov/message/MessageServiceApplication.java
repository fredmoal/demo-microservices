package fr.univ.orleans.innov.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class MessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }

}
