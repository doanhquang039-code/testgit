package com.example.hr.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP Aspect tự động logging cho service layer.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(com.example.hr.service..*)")
    public void serviceMethods() {}

    @Pointcut("within(com.example.hr.api..*)")
    public void apiMethods() {}

    /**
     * Log entry/exit/duration/exception cho tất cả service methods.
     */
    @Around("serviceMethods()")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.debug(">>> {}.{}() - args: {}", className, methodName,
                summarizeArgs(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            log.debug("<<< {}.{}() - {}ms", className, methodName, duration);

            // Warn if slow
            if (duration > 1000) {
                log.warn("Slow service call: {}.{}() took {}ms", className, methodName, duration);
            }

            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("!!! {}.{}() failed after {}ms: {}", className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    /**
     * Log API calls.
     */
    @Around("apiMethods()")
    public Object logApiMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("API >>> {}.{}()", className, methodName);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("API <<< {}.{}() - {}ms", className, methodName, duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("API !!! {}.{}() failed after {}ms: {}", className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    private String summarizeArgs(Object[] args) {
        if (args == null || args.length == 0) return "[]";
        return Arrays.stream(args)
                .map(a -> a == null ? "null" : a.getClass().getSimpleName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}
