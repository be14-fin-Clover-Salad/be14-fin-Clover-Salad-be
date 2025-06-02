package com.clover.salad.employee.query.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;

@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

	private final EmployeeMapper employeeMapper;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeQueryServiceImpl(EmployeeMapper employeeMapper,
		EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
	}

	@Override
	public List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchEmployeeDTO) {
		return employeeMapper.searchEmployees(searchEmployeeDTO);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EmployeeEntity employee = employeeRepository.findByCode(username)
			.orElseThrow(() -> new UsernameNotFoundException("해당 사번을 가진 사용자를 찾을 수 없습니다: " + username));

		System.out.println("[DEBUG] 로그인 사용자 로드됨: " + employee.getCode() + ", isAdmin: " + employee.isAdmin());

		return new User(
			employee.getCode(),
			employee.getEncPwd(),
			getAuthorities(employee)
		);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(EmployeeEntity employee) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		if (employee.isAdmin()) {
			System.out.println("[DEBUG] 권한 부여: ROLE_ADMIN");
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			System.out.println("[DEBUG] 권한 부여: ROLE_MEMBER");
			authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		}
		return authorities;
	}
}