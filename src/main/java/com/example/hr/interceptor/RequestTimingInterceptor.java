package com.example.hr.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor đo thời gian xử lý mỗi request.
 */
@Component
public class RequestTimingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestTimingInterceptor.class);
    private static final String START_TIME_ATTR = "requestStartTime";
    private static final long SLOW_REQUEST_THRESHOLD_MS = 2000; // 2 seconds

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                  Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            String method = request.getMethod();
            String uri = request.getRequestURI();
            int status = response.getStatus();

            if (duration > SLOW_REQUEST_THRESHOLD_MS) {
                log.warn("SLOW REQUEST: {} {} - {}ms (status: {})", method, uri, duration, status);
            } else {
                log.debug("Request: {} {} - {}ms (status: {})", method, uri, duration, status);
            }
        }
    }
}
