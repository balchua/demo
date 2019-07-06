package org.bal.demo.config;


import brave.CurrentSpanCustomizer;
import brave.SpanCustomizer;
import brave.Tracing;
import brave.http.HttpTracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;


@SpringBootConfiguration
public class Configuration  {

    @Value("${zipkin.host}")
    private String zipkinHost;

    @Value("${zipkin.port}")
    private int zipkinPort;

    @Value("${zipkin.samplingRate}")
    private float zipkinSamplingRate;

    /**
     * Configuration for how to buffer spans into messages for Zipkin
     */
    @Bean
    public Reporter<Span> spanReporter() {
        return AsyncReporter.builder(sender()).build();
    }


    /** Controls aspects of tracing such as the service name that shows up in the UI */
    @Bean Tracing tracing(@Value("${spring.application.name}") String serviceName) {
        return Tracing.newBuilder()
                .localServiceName(serviceName)
                .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "user-name"))
                .sampler(Sampler.create(zipkinSamplingRate))
                .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder()
                        .build()
                )
                .spanReporter(spanReporter()).build();
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


    /** Allows someone to add tags to a span if a trace is in progress */
    @Bean
    public SpanCustomizer spanCustomizer(Tracing tracing) {
        return CurrentSpanCustomizer.create(tracing);
    }



}
