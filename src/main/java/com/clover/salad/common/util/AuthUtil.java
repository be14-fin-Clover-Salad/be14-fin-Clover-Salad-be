package com.clover.salad.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.clover.salad.security.token.TokenPrincipal;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    /** 현재 HTTP 요청의 Authorization 헤더에서 Bearer 토큰 값을 추출 */
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

    /** 현재 로그인된 사용자의 권한 문자열 (예: ROLE_ADMIN 등) 반환 */
    public static String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) {
            throw new IllegalStateException("인증 정보가 없습니다. 로그인이 필요합니다.");
        }
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .filter(r -> r.startsWith("ROLE_")).findFirst()
                .orElseThrow(() -> new IllegalStateException("유효한 ROLE이 없습니다."));
    }

    public static boolean isAdmin() {
        return "ROLE_ADMIN".equals(getRole());
    }

    public static boolean isManager() {
        return "ROLE_MANAGER".equals(getRole());
    }

    public static boolean isMember() {
        return "ROLE_MEMBER".equals(getRole());
    }

    public static void assertAdmin() {
        if (!isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }

    public static void assertManager() {
        if (!isManager()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "팀장 권한이 필요합니다.");
        }
    }

    public static void assertMember() {
        if (!isMember()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "사원 권한이 필요합니다.");
        }
    }

    /** 현재 로그인된 사용자의 사번(code) 반환 */
    public static String getEmployeeCode() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null
                || !(auth.getPrincipal() instanceof TokenPrincipal)) {
            throw new IllegalStateException("유효한 로그인 정보가 없습니다.");
        }
        return ((TokenPrincipal) auth.getPrincipal()).getCode();
    }

    /** 현재 로그인된 사용자의 내부 식별자(employeeId) 반환 */
    public static int getEmployeeId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null
                || !(auth.getPrincipal() instanceof TokenPrincipal)) {
            throw new IllegalStateException("유효한 로그인 정보가 없습니다.");
        }
        return ((TokenPrincipal) auth.getPrincipal()).getEmployeeId();
    }
}
