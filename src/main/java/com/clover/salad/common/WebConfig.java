package com.clover.salad.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.clover.salad.common.logging.ClientLogInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(
				"http://localhost:8080",
				"http://localhost:5173",
				"http://salad-alb-240627784.ap-northeast-2.elb.amazonaws.com",
				"https://salad-alb-240627784.ap-northeast-2.elb.amazonaws.com"
			)
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
			.allowedHeaders("*")
			.exposedHeaders("Authorization")
			.allowCredentials(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ClientLogInterceptor())
			.addPathPatterns("/**");
	}
}

