package com.clover.salad.employee.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.query.dto.DepartmentEmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.EmployeeDetailDTO;
import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchRequestDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;

@Mapper
public interface EmployeeMapper {

	// 사원 검색
	List<EmployeeSearchResponseDTO> searchEmployees(EmployeeSearchRequestDTO requestDTO);

	// 모든 관리자 검색
	List<Integer> findAdminIds();

	// 비밀번호 재설정 로직 중
	Integer selectIdByCodeAndEmail(@Param("code") String code, @Param("email") String email);

	// 로그인 시 헤더 정보 조회
	LoginHeaderInfoDTO findLoginHeaderInfoById(@Param("id") int id);

	// 관리자 여부 확인
	Boolean selectIsAdminById(@Param("id") int id);

	// 마이페이지 정보 조회
	EmployeeMypageQueryDTO selectMyPageInfoById(@Param("id") int id);

	// 사원 이름 조회 추가
	String findNameById(@Param("id") int id);

	/* 설명. 사원이 결재를 하는 경우 자신의 팀장을 찾는 로직에 사용하는 메서드들 */
	Integer findDepartmentIdByEmployeeId(@Param("employeeId") int employeeId);
	Integer findManagerIdByDeptId(@Param("deptId") int deptId);

	EmployeeQueryDTO findEmployeeById(@Param("id") int id);

	List<EmployeeSearchResponseDTO> searchEmployeesByDeptId(int deptId);

	List<DepartmentEmployeeSearchResponseDTO> searchEmployeesByDeptIds(@Param("deptIds") List<Integer> deptIds);

	// 사원 상세 조회 페이지
	EmployeeDetailDTO findEmployeeDetailById(@Param("id") int id);
}
