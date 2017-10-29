package org.bal.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@SpringBootApplication
@RestController
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @RequestMapping("/")
    public String home() {
        LocalDateTime currentDate = LocalDateTime.now();
        return "Hello World! -[" + currentDate + "]";
    }

    @RequestMapping("/peron/$id")
    public String person(String id) {

        return "Person is: " + id;
    }
}
