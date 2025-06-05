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

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.employee.command.domain.aggregate.entity.DepartmentEntity;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.aggregate.enums.EmployeeLevel;
import com.clover.salad.employee.command.domain.repository.DepartmentRepository;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

	private final EmployeeMapper employeeMapper;
	private final EmployeeRepository employeeRepository;
	private final FileUploadRepository fileUploadRepository;
	private final DepartmentRepository departmentRepository;

	@Autowired
	public EmployeeQueryServiceImpl(EmployeeMapper employeeMapper,
		EmployeeRepository employeeRepository,
		FileUploadRepository fileUploadRepository,
		DepartmentRepository departmentRepository) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
		this.fileUploadRepository = fileUploadRepository;
		this.departmentRepository = departmentRepository;
	}

	@Override
	public List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchEmployeeDTO) {
		return employeeMapper.searchEmployees(searchEmployeeDTO);
	}

	@Override
	public boolean checkIsAdmin(String code) {
		return employeeMapper.selectIsAdminByCode(code);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EmployeeEntity employee = employeeRepository.findByCode(username)
			.orElseThrow(() -> new UsernameNotFoundException("해당 사번을 가진 사용자를 찾을 수 없습니다: " + username));

		log.info("로그인 사용자 로드됨: {}, isAdmin: {}", employee.getCode(), employee.isAdmin());

		return new User(
			employee.getCode(),
			employee.getEncPwd(),
			getAuthorities(employee)
		);
	}

	@Override
	public LoginHeaderInfoDTO getLoginHeaderInfo(String code) {
		EmployeeEntity employee = employeeRepository.findByCode(code)
			.orElseThrow(() -> new RuntimeException("사원을 찾을 수 없습니다."));

		String name = employee.getName();
		String levelLabel = employee.getLevel().getLabel();

		FileUploadEntity file = fileUploadRepository.findById(employee.getProfile()).orElse(null);
		String profilePath = file != null ? file.getPath() : null;

		DepartmentEntity dept = departmentRepository.findById(employee.getDepartmentId()).orElse(null);
		String deptName = dept != null ? dept.getName() : null;

		return new LoginHeaderInfoDTO(name, levelLabel, profilePath, deptName);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(EmployeeEntity employee) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		// 관리자 여부
		if (employee.isAdmin()) {
			log.info("권한 부여: ROLE_ADMIN");
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			log.info("권한 부여: ROLE_MEMBER");
			authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		}

		// 팀장 여부
		if (employee.getLevel() == EmployeeLevel.L5) {
			log.info("직급이 팀장 → ROLE_MANAGER 권한 추가 부여");
			authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		}

		return authorities;
	}
}
