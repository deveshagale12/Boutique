package com.boutique;

import com.boutique.interceptor.ApiKeyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    // Constructor Injection (Recommended over @Autowired)
    public WebConfig(ApiKeyInterceptor apiKeyInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS).
     * This allows your HTML frontend (Live Server or Render) to access this API.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // List of allowed origins (where your index.html is hosted)
                .allowedOrigins(
                    "http://127.0.0.1:5500", 
                    "http://localhost:5500", 
                    "http://localhost:8000",
                    "https://boutique-frontend.onrender.com" // Update with your actual frontend URL
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache the CORS response for 1 hour
    }

    /**
     * Registers the Security Interceptor.
     * This ensures every request to /api/boutique/** has a valid X-API-KEY.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/boutique/**");
    }
}