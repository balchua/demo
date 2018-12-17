package org.bal.app;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class MainApplication {

    public static void main(String[] args) {
        System.out.println("starting the app.  Im using Jib");
        SpringApplication.run(MainApplication.class, args);
    }


}
