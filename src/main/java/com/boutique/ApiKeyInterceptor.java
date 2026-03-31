package com.boutique;

import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${boutique.api.key:DEFAULT_KEY_CHANGE_ME}")
    private String expectedApiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. Skip check for CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String actualApiKey = request.getHeader("X-API-KEY");

        // 2. DEBUG PRINT - Check your IntelliJ/Eclipse console for these!
        System.out.println("--- BOUTIQUE API KEY CHECK ---");
        System.out.println("Value in Properties: [" + expectedApiKey + "]");
        System.out.println("Value in Request:    [" + actualApiKey + "]");

        // 3. Comparison with Null Safety
        if (actualApiKey != null && expectedApiKey.trim().equals(actualApiKey.trim())) {
            return true; 
        }

        // 4. Return JSON Error
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Invalid API Key\", \"status\": \"401\"}");
        
        return false;
    }
}