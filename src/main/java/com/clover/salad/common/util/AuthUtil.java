package com.clover.salad.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class AuthUtil {

    // 현재 로그인된 사용자의 권한 문자열 (예: ROLE_ADMIN 등) 반환
    public static String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .filter(r -> r.startsWith("ROLE_")).findFirst().orElse("ROLE_MEMBER"); // 기본값
    }

    // 권한이 ROLE_ADMIN인지 확인
    public static boolean isAdmin() {
        return "ROLE_ADMIN".equals(getRole());
    }

    // 권한이 ROLE_MANAGER인지 확인
    public static boolean isManager() {
        return "ROLE_MANAGER".equals(getRole());
    }

    // 권한이 ROLE_MEMBER인지 확인
    public static boolean isMember() {
        return "ROLE_MEMBER".equals(getRole());
    }

    // 관리자 권한이 아니면 예외 발생
    public static void assertAdmin() {
        if (!isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }

    // 매니저 권한이 아니면 예외 발생
    public static void assertManager() {
        if (!isManager()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "매니저 권한이 필요합니다.");
        }
    }

    // 일반 사용자 권한이 아니면 예외 발생
    public static void assertMember() {
        if (!isMember()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일반 사용자 권한이 필요합니다.");
        }
    }
}
