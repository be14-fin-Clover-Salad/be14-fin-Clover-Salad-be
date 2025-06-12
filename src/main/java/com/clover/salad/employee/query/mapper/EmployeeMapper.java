package com.clover.salad.employee.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;

@Mapper
public interface EmployeeMapper {

	// 사원 검색
	List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchEmployeeDTO);

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

	EmployeeQueryDTO findEmployeeById(@Param("id") int id);
}
