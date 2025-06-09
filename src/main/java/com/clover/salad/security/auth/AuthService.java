package com.clover.salad.security.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
	UserDetails loadUserById(int id);

	String findCodeByEmployeeId(int id);
}