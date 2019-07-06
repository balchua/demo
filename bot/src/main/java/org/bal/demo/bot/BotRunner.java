package org.bal.demo.bot;

import lombok.extern.slf4j.Slf4j;
import org.bal.demo.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackageClasses = {Configuration.class}, scanBasePackages = "org.bal.demo.bot")
@EnableScheduling
@Slf4j
public class BotRunner  {

    public static void main(String[] args) {
        log.info("Bot Starting");
        SpringApplication.run(BotRunner.class, args);

    }


}