package com.clover.salad.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 현재 HTTP 요청의 Authorization 헤더에서 Bearer 토큰 값을 추출
     * 
     * @return 토큰 문자열 (접두어 제거 후 반환), 없으면 null
     */
    public static String resolveToken() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null)
            return null;

        HttpServletRequest request = attributes.getRequest();
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * 현재 로그인된 사용자의 권한 문자열 (예: ROLE_ADMIN 등) 반환
     */
    public static String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getAuthorities().isEmpty()) {
            throw new IllegalStateException("인증 정보가 없습니다. 로그인이 필요합니다.");
        }

        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .filter(r -> r.startsWith("ROLE_")).findFirst()
                .orElseThrow(() -> new IllegalStateException("유효한 ROLE이 없습니다."));
    }

    /**
     * 권한이 ROLE_ADMIN인지 확인
     */
    public static boolean isAdmin() {
        return "ROLE_ADMIN".equals(getRole());
    }

    /**
     * 권한이 ROLE_MANAGER인지 확인
     */
    public static boolean isManager() {
        return "ROLE_MANAGER".equals(getRole());
    }

    /**
     * 권한이 ROLE_MEMBER인지 확인
     */
    public static boolean isMember() {
        return "ROLE_MEMBER".equals(getRole());
    }

    /**
     * 관리자 권한이 아니면 예외 발생
     */
    public static void assertAdmin() {
        if (!isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }

    /**
     * 팀장 권한이 아니면 예외 발생
     */
    public static void assertManager() {
        if (!isManager()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "팀장 권한이 필요합니다.");
        }
    }

    /**
     * 일반 사용자 권한이 아니면 예외 발생
     */
    public static void assertMember() {
        if (!isMember()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일반 사용자 권한이 필요합니다.");
        }
    }
}
