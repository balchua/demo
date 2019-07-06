package org.bal.demo.bot;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class Tracer {
//    @Autowired
//    private HttpTracing httpTracing;
//
//    private brave.Tracer tracer;


    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void doIt(ProceedingJoinPoint pjp) throws Throwable {
//        tracer = httpTracing.tracing().tracer();

        log.debug("before");
//        httpTracing.
        pjp.proceed();
        log.debug("After");
    }
}