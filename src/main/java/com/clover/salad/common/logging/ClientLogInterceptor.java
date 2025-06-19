package com.clover.salad.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class ClientLogInterceptor implements HandlerInterceptor {

	private static final Logger accessLogger = LoggerFactory.getLogger("ACCESS_LOGGER");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String ip = request.getRemoteAddr();
		String uri = request.getRequestURI();
		String method = request.getMethod();
		String userAgent = request.getHeader("User-Agent");

		String userId = "Anonymous";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
			&& !"anonymousUser".equals(authentication.getPrincipal())) {
			userId = authentication.getName();
		}

		accessLogger.info("Client Request - User: {}, IP: {}, Method: {}, URI: {}, User-Agent: {}",
			userId, ip, method, uri, userAgent);

		return true;
	}

}
