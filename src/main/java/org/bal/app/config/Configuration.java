package org.bal.app.config;


import brave.Tracing;
import brave.grpc.GrpcTracing;
import brave.http.HttpTracing;
import brave.spring.webmvc.TracingHandlerInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.bal.app.proto.internal.PersonManagementGrpc;
import org.bal.app.proto.internal.PersonManagementGrpc.PersonManagementBlockingStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;


@org.springframework.context.annotation.Configuration
@ComponentScan("org.bal.app.client")
// Importing these classes is effectively the same as declaring bean methods
@Import({TracingHandlerInterceptor.class})
public class Configuration extends WebMvcConfigurerAdapter {

    @Value("${zipkin.host}")
    private String zipkinHost;

    @Value("${zipkin.port}")
    private int zipkinPort;

    @Value("${person-service.host}")
    private String personServiceHost;

    @Value("${person-service.port}")
    private int personServicePort;

    @Autowired
    private TracingHandlerInterceptor serverInterceptor;

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
                .localServiceName("person-rest-service")
                .spanReporter(reporter()).build();
    }

    // decides how to name and tag spans. By default they are named the same as the http method.
    @Bean
    public HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing);
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


    @Bean
    public ManagedChannelBuilder managedChannelBuilder() {
        return ManagedChannelBuilder.forAddress(personServiceHost, personServicePort).intercept(grpcTracing().newClientInterceptor())
                .usePlaintext(true);
    }

    @Bean
    public ManagedChannel managedChannel() {
        return managedChannelBuilder().build();
    }

    @Bean("personManagementBlockingStub")
    public PersonManagementBlockingStub personManagementBlockingStub() {
        return PersonManagementGrpc.newBlockingStub(managedChannel());
    }

    /**
     * adds tracing to the application-defined web controller
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverInterceptor).excludePathPatterns("/health");
    }
}
