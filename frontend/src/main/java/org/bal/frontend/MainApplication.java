package org.bal.frontend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.bal.frontend")
@Slf4j
public class MainApplication {

    public static void main(String[] args) {
       log.debug("Starting the demo");
        SpringApplication.run(MainApplication.class, args);
    }


}
