package com.clover.salad.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;

@Getter
public class EmployeeDetails implements UserDetails {

	private final int id;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	public EmployeeDetails(int id, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return String.valueOf(id);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() { return true; }
}