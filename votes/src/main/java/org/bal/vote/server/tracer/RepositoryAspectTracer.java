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
        ScopedSpan span = getSpan(traced);
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        traceMethodName(span, traced, signature);
        try {
            return pjp.proceed();
        } catch (RuntimeException | Error e) {
            logErrorToSpan(e, span);
            throw e;
        } finally {
            log.debug("After");
            completeSpan(span);
        }


    }

    private void traceMethodName(ScopedSpan span, Traced traced, MethodSignature signature) {
        if (span != null) {
            span.tag("method", signature.getName());
        }
    }

    private void completeSpan(ScopedSpan span) {
        if (span != null) {
            span.finish();
        }
    }

    private void logErrorToSpan(Throwable e, ScopedSpan span) {
        if (span != null) {
            span.error(e);
        }
    }

    private ScopedSpan getSpan(Traced traced) {
        ScopedSpan span = null;
        if (tracer.currentSpan() != null) {
            span = tracer.startScopedSpan(traced.serviceName());
        }

        return span;
    }

}
