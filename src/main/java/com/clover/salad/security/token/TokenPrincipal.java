package com.clover.salad.security.token;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class TokenPrincipal implements UserDetails {
	private final int employeeId;
	private final String code;
	private final Collection<? extends GrantedAuthority> authorities;

	public TokenPrincipal(int employeeId, String code, Collection<? extends GrantedAuthority> authorities) {
		this.employeeId = employeeId;
		this.code = code;
		this.authorities = authorities;
	}

	@Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
	@Override public String getPassword() { return null; }
	@Override public String getUsername() { return code; } // 유저가 입력하는 식별자 = 사번
	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() { return true; }
}