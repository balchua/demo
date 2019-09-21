package org.bal.frontend.config;


import brave.CurrentSpanCustomizer;
import brave.SpanCustomizer;
import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.grpc.GrpcTracing;
import brave.http.HttpAdapter;
import brave.http.HttpSampler;
import brave.http.HttpTracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import brave.servlet.TracingFilter;
import brave.spring.webmvc.SpanCustomizingAsyncHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.servlet.Filter;


@org.springframework.context.annotation.Configuration
@ComponentScan("org.bal.app.client")
@Import({SpanCustomizingAsyncHandlerInterceptor.class})
public class Configuration implements WebMvcConfigurer {

    @Value("${zipkin-server.host}")
    private String zipkinHost;

    @Value("${zipkin-server.port}")
    private int zipkinPort;

    @Value("${zipkin-server.samplingRate}")
    private float zipkinSamplingRate;

    @Autowired
    private SpanCustomizingAsyncHandlerInterceptor serverInterceptor;

    /**
     * Configuration for how to buffer spans into messages for Zipkin
     */
    @Bean
    public Reporter<Span> spanReporter() {
        return AsyncReporter.builder(sender()).build();
    }


    /**
     * Controls aspects of tracing such as the service name that shows up in the UI
     */
    @Bean
    Tracing tracing(@Value("${spring.application.name}") String serviceName) {
        return Tracing.newBuilder()
                .localServiceName(serviceName)
                .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "user-name"))
                .sampler(Sampler.create(zipkinSamplingRate))
                .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder().addScopeDecorator(MDCScopeDecorator.create())
                        .build()
                )
                .spanReporter(spanReporter()).build();
    }

    // Exclude certain paths from being traced.
    @Bean
    public HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing).toBuilder().serverSampler(new HttpSampler() {
            @Override
            public <Req> Boolean trySample(HttpAdapter<Req, ?> httpAdapter, Req request) {
                String path = httpAdapter.path(request);
                if (path.startsWith("/favicon") || path.startsWith("/actuator") || path.startsWith("/healthz")) {
                    return false;
                }
                return null; // defer decision to probabilistic on trace ID
            }
        }).build();
    }

    /**
     * Configuration for how to send spans to Zipkin
     */
    @Bean
    public Sender sender() {
        return OkHttpSender.create("http://" + zipkinHost + ":" + zipkinPort + "/api/v2/spans");
    }

    @Bean
    public GrpcTracing grpcTracing(Tracing tracing) {
        return GrpcTracing.create(tracing);
    }

    /**
     * Allows someone to add tags to a span if a trace is in progress
     */
    @Bean
    public SpanCustomizer spanCustomizer(Tracing tracing) {
        return CurrentSpanCustomizer.create(tracing);
    }


    /**
     * Creates server spans for http requests
     */
    @Bean
    public Filter tracingFilter(HttpTracing httpTracing) {
        return TracingFilter.create(httpTracing);
    }


    @Bean(name = "htmlTemplateEngine")
    public TemplateEngine htmlTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(textTemplateResolver());
        return templateEngine;
    }

    private ITemplateResolver textTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    /**
     * adds tracing to the application-defined web controller
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverInterceptor);
    }
}
