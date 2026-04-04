package com.chatbot.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Logs every HTTP request and response for debugging and audit purposes.
 */
@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        long start = System.currentTimeMillis();

        log.info("➡️  {} {} [IP: {}]",
                req.getMethod(), req.getRequestURI(), req.getRemoteAddr());

        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - start;
        log.info("⬅️  {} {} → {} [{}ms]",
                req.getMethod(), req.getRequestURI(), res.getStatus(), duration);
    }
}
