package com.clover.salad.employee.query.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeQueryServiceImpl implements EmployeeQueryService{

	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	@Autowired
	public EmployeeQueryServiceImpl(EmployeeRepository employeeRepository,
									ModelMapper modelMapper,
									BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.employeeRepository = employeeRepository;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public EmployeeQueryDTO getEmployeeById(String employeeId) {

		EmployeeEntity foundEmployee = employeeRepository.findById(Integer.parseInt(employeeId)).get();

		EmployeeQueryDTO employeeQueryDTO = modelMapper.map(foundEmployee, EmployeeQueryDTO.class);

		return employeeQueryDTO;
	}

	@Override
	public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {

		EmployeeEntity loginUser = employeeRepository.findByCode(code);

		if(loginUser == null) {
			throw new UsernameNotFoundException(code + " 해당 사번은 존재하지 않습니다.");
		}

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));

		return new User(loginUser.getCode(), loginUser.getEncPwd(),
			true, true, true, true, grantedAuthorities);
	}
}
