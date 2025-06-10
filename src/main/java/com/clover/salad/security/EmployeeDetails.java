package com.clover.salad.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;

@Getter
public class EmployeeDetails implements UserDetails {

	private final int id;
	private final String code; // 사번
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	public EmployeeDetails(int id, String code, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.code = code;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return code; // 스프링 시큐리티의 username은 사번으로 설정
	}

	@Override public String getPassword() { return password; }
	@Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() { return true; }
}