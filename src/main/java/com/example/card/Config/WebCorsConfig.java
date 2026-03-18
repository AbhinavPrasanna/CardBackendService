package com.example.card.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/graphql")
        .allowedOriginPatterns(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "http://10.0.0.*:*",
            "http://192.168.*:*")
        .allowedMethods("GET", "POST", "OPTIONS")
        .allowedHeaders("*");
  }
}
