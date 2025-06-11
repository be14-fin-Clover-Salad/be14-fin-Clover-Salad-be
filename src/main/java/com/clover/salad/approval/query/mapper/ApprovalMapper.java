package com.clover.salad.approval.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.approval.query.dto.ApprovalDetailDTO;
import com.clover.salad.approval.query.dto.ApprovalExistenceCheckDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;

@Mapper
public interface ApprovalMapper {

	/* 설명. 결재 내역 목록 조회 */
	// 관리자
	List<ApprovalSearchResponseDTO> searchApprovals(ApprovalSearchRequestDTO request);

	// 사원
	List<ApprovalSearchResponseDTO> searchByRequester(ApprovalSearchRequestDTO request);

	// 팀장
	List<ApprovalSearchResponseDTO> searchByApprover(ApprovalSearchRequestDTO request);

	/* 설명. 결재 내역 상세 조회 */
	ApprovalDetailDTO findApprovalDetailById(@Param("id") int id);

	/* 설명. 결재 코드 생성을 위해 해당 월의 마지막 결재 코드 찾는 로직 */
	String findLastCodeByPrefix(@Param("prefix") String prefix);

	int countByCode(String newCode);

	/* 설명. 계약에 대한 기존 결재 유무 체크 */
	int countExistingApprovalRequest(ApprovalExistenceCheckDTO dto);
}
