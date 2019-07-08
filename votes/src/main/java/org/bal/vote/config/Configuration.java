package org.bal.vote.config;


import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.grpc.ServerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;


@SpringBootConfiguration
public class Configuration {

    @Value("${zipkin-server.host}")
    private String zipkinHost;

    @Value("${zipkin-server.port:9411}")
    private int zipkinPort;

    /**
     * Configuration for how to buffer spans into messages for Zipkin
     */
    @Bean
    public Reporter<Span> reporter() {
        return AsyncReporter.builder(sender()).build();
    }

    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder()
                .localServiceName("vote-service")
                .spanReporter(reporter()).build();
    }

    /**
     * Configuration for how to send spans to Zipkin
     */
    @Bean
    public Sender sender() {
        return OkHttpSender.create("http://" + zipkinHost + ":" + zipkinPort + "/api/v2/spans");
    }

    @Bean
    public GrpcTracing grpcTracing() {
        return GrpcTracing.create(tracing());
    }

    @Bean(name = "grpcServerInterceptor")
    public ServerInterceptor grpcServerInterceptor() {

        return grpcTracing().newServerInterceptor();
    }

}
