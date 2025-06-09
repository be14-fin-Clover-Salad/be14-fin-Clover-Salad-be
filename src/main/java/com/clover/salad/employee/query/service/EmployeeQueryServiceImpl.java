package com.clover.salad.employee.query.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.employee.command.domain.aggregate.entity.DepartmentEntity;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.aggregate.enums.EmployeeLevel;
import com.clover.salad.employee.command.domain.repository.DepartmentRepository;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.security.EmployeeDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService, UserDetailsService {

	private final EmployeeMapper employeeMapper;
	private final EmployeeRepository employeeRepository;
	private final FileUploadRepository fileUploadRepository;
	private final DepartmentRepository departmentRepository;

	@Autowired
	public EmployeeQueryServiceImpl(EmployeeMapper employeeMapper,
		EmployeeRepository employeeRepository,
		FileUploadRepository fileUploadRepository,
		DepartmentRepository departmentRepository) {
		this.employeeMapper = employeeMapper;
		this.employeeRepository = employeeRepository;
		this.fileUploadRepository = fileUploadRepository;
		this.departmentRepository = departmentRepository;
	}

	@Override
	public List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchEmployeeDTO) {
		return employeeMapper.searchEmployees(searchEmployeeDTO);
	}

	@Override
	public boolean checkIsAdminById(int id) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 ID를 가진 사용자를 찾을 수 없습니다."));

		return employee.isAdmin();
	}

	@Override
	public String findCodeById(int id) {
		return employeeRepository.findById(id)
			.map(EmployeeEntity::getCode)
			.orElseThrow(() -> new RuntimeException("사번 조회 실패: " + id));
	}

	@Override
	public UserDetails loadUserById(int id) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

		log.info("[EmployeeDetails 생성] ID: {}, CODE: {}, AUTH: {}", employee.getId(), employee.getCode(), getAuthorities(employee));
		log.info("[loadUserById] code={}", employee.getCode());
		return new EmployeeDetails(
			employee.getId(),
			employee.getCode(),
			employee.getPassword(),
			getAuthorities(employee)
		);
	}

	@Override
	public LoginHeaderInfoDTO getLoginHeaderInfoById(int id) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("사원을 찾을 수 없습니다."));

		String name = employee.getName();
		String levelLabel = employee.getLevel().getLabel();

		FileUploadEntity file = fileUploadRepository.findById(employee.getProfile()).orElse(null);
		String profilePath = file != null ? file.getPath() : null;

		DepartmentEntity dept = departmentRepository.findById(employee.getDepartmentId()).orElse(null);
		String deptName = dept != null ? dept.getName() : null;

		return new LoginHeaderInfoDTO(name, levelLabel, profilePath, deptName);
	}

	@Override
	public EmployeeMypageQueryDTO getMyPageInfoById(int id) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("사원을 찾을 수 없습니다."));

		FileUploadEntity file = fileUploadRepository.findById(employee.getProfile()).orElse(null);
		DepartmentEntity dept = departmentRepository.findById(employee.getDepartmentId()).orElse(null);

		EmployeeMypageQueryDTO dto = new EmployeeMypageQueryDTO();
		dto.setCode(employee.getCode()); // 여전히 프론트 표시용으로 code 유지
		dto.setName(employee.getName());
		dto.setPhone(employee.getPhone());
		dto.setEmail(employee.getEmail());
		dto.setLevel(employee.getLevel().getLabel());
		dto.setHireDate(employee.getHireDate());
		dto.setWorkPlace(employee.getWorkPlace());
		dto.setProfilePath(file != null ? file.getPath() : null);
		dto.setDepartmentName(dept != null ? dept.getName() : null);

		return dto;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(EmployeeEntity employee) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		if (employee.isAdmin()) {
			log.info("권한 부여: ROLE_ADMIN");
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		}

		if (employee.getLevel() == EmployeeLevel.L5) {
			log.info("직급이 팀장 → ROLE_MANAGER 권한 추가 부여");
			authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		}

		return authorities;
	}

	@Override
	public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
		EmployeeEntity employee = employeeRepository.findByCode(code)
			.orElseThrow(() -> new UsernameNotFoundException("사번에 해당하는 사용자를 찾을 수 없습니다."));

		return new EmployeeDetails(
			employee.getId(),
			employee.getCode(),
			employee.getPassword(),
			getAuthorities(employee)
		);
	}
}