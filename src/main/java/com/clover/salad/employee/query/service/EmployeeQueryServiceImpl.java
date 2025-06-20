package com.clover.salad.employee.query.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.employee.command.domain.aggregate.entity.DepartmentEntity;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.aggregate.enums.EmployeeLevel;
import com.clover.salad.employee.command.domain.repository.DepartmentRepository;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.DepartmentEmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.DepartmentHierarchyDTO;
import com.clover.salad.employee.query.dto.EmployeeDetailDTO;
import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchRequestDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.mapper.DepartmentMapper;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.security.EmployeeDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService, UserDetailsService {

	private final EmployeeMapper employeeMapper;
	private final DepartmentMapper departmentMapper;

	private final EmployeeRepository employeeRepository;
	private final FileUploadRepository fileUploadRepository;
	private final DepartmentRepository departmentRepository;

	@Autowired
	public EmployeeQueryServiceImpl(EmployeeMapper employeeMapper,
		DepartmentMapper departmentMapper,
		EmployeeRepository employeeRepository,
		FileUploadRepository fileUploadRepository,
		DepartmentRepository departmentRepository
	) {
		this.employeeMapper = employeeMapper;
		this.departmentMapper = departmentMapper;
		this.employeeRepository = employeeRepository;
		this.fileUploadRepository = fileUploadRepository;
		this.departmentRepository = departmentRepository;
	}

	@Override
	public List<EmployeeSearchResponseDTO> searchEmployees(EmployeeSearchRequestDTO requestDTO) {
		return employeeMapper.searchEmployees(requestDTO);
	}

	// @Override
	// public boolean checkIsAdminById(int id) {
	// 	EmployeeEntity employee = employeeRepository.findById(id)
	// 		.orElseThrow(() -> new RuntimeException("해당 ID를 가진 사용자를 찾을 수 없습니다."));
	//
	// 	return employee.isAdmin();
	// }

	@Override
	public String findCodeById(int id) {
		return employeeRepository.findById(id)
			.map(EmployeeEntity::getCode)
			.orElseThrow(() -> new RuntimeException("사번 조회 실패: " + id));
	}

	@Override
	public String findNameById(int id) {
		String name = employeeMapper.findNameById(id);
		if (name == null) {
			throw new IllegalArgumentException("해당 ID의 사원을 찾을 수 없습니다: " + id);
		}
		return name;

	}

	@Override
	@Transactional(readOnly = true)
	public List<DepartmentEmployeeSearchResponseDTO> searchEmployeesByDepartmentWithSub(int departmentId) {
		List<DepartmentHierarchyDTO> subDepartments = departmentMapper.selectSubDepartments(departmentId);
		List<Integer> deptIds = subDepartments.stream().map(DepartmentHierarchyDTO::getId).toList();
		return employeeMapper.searchEmployeesByDeptIds(deptIds);
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
		LoginHeaderInfoDTO rawDto = employeeMapper.findLoginHeaderInfoById(id);

		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("사원을 찾을 수 없습니다."));
		String levelLabel = employee.getLevel().getLabel();

		return new LoginHeaderInfoDTO(
			rawDto.getId(),
			rawDto.getName(),
			levelLabel,
			rawDto.getProfilePath(),
			rawDto.getDepartmentName()
		);
	}

	@Override
	public EmployeeMypageQueryDTO getMyPageInfoById(int id) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("사원을 찾을 수 없습니다."));

		FileUploadEntity file = fileUploadRepository.findById(employee.getProfile()).orElse(null);
		DepartmentEntity dept = departmentRepository.findById(employee.getDepartmentId()).orElse(null);

		EmployeeMypageQueryDTO dto = new EmployeeMypageQueryDTO();
		dto.setCode(employee.getCode());
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

	@Override
	public EmployeeDetailDTO getEmployeeDetailById(int id) {
		EmployeeDetailDTO dto = employeeMapper.findEmployeeDetailById(id);
		if (dto == null) {
			throw new IllegalArgumentException("해당 ID의 사원이 존재하지 않습니다: " + id);
		}
		return dto;
	}
}
