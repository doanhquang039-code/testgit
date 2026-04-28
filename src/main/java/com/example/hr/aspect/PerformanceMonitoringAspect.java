package com.example.hr.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect — tự động đo thời gian xử lý mỗi Controller/Service method.
 * Log WARN nếu method chạy > 500ms (slow operation).
 */
@Aspect
@Component
public class PerformanceMonitoringAspect {

    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);
    private static final long SLOW_THRESHOLD_MS = 500;

    /** Pointcut: tất cả public methods trong controllers package */
    @Pointcut("execution(public * com.example.hr.controllers..*(..))")
    public void controllerMethods() {}

    /** Pointcut: tất cả public methods trong service package */
    @Pointcut("execution(public * com.example.hr.service..*(..))")
    public void serviceMethods() {}

    /**
     * Đo thời gian xử lý Controller methods.
     * Log INFO nếu bình thường, WARN nếu > 500ms.
     */
    @Around("controllerMethods()")
    public Object measureControllerTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint, "CTRL");
    }

    /**
     * Đo thời gian xử lý Service methods.
     */
    @Around("serviceMethods()")
    public Object measureServiceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint, "SVC");
    }

    private Object measureExecutionTime(ProceedingJoinPoint joinPoint, String prefix) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();

        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - startTime;

            if (elapsed > SLOW_THRESHOLD_MS) {
                log.warn("[{}] SLOW ⚠️  {} took {}ms (threshold: {}ms)",
                        prefix, methodName, elapsed, SLOW_THRESHOLD_MS);
            } else {
                log.debug("[{}] {} took {}ms", prefix, methodName, elapsed);
            }

            return result;
        } catch (Throwable ex) {
            long elapsed = System.currentTimeMillis() - startTime;
            log.error("[{}] ERROR in {} after {}ms — {}: {}",
                    prefix, methodName, elapsed, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }
}
