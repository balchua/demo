package org.bal.vote.server.tracer;


import brave.ScopedSpan;
import brave.Tracer;
import brave.Tracing;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.bal.vote.annotation.Traced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class RepositoryAspectTracer {

    private final Tracer tracer;

    public RepositoryAspectTracer(@Autowired Tracing tracing) {
        tracer = tracing.tracer();
    }


    @Around("@annotation(traced)")
    public Object traceRepository(ProceedingJoinPoint pjp, Traced traced) throws Throwable {

        log.debug("Tracing repository calls.");
        ScopedSpan span = tracer.startScopedSpan(traced.serviceName());
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        span.tag("method", signature.getName());
        try {
            return pjp.proceed();
        } catch (RuntimeException | Error e) {
            span.error(e); // Unless you handle exceptions, you might not know the operation failed!
            throw e;
        } finally {
            log.debug("After");
            span.finish(); // always finish the span
        }


    }

}
