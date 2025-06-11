package com.clover.salad.approval.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;

@Mapper
public interface ApprovalMapper {

	// 관리자
	List<ApprovalSearchResponseDTO> searchApprovals(ApprovalSearchRequestDTO request);

	// 사원
	List<ApprovalSearchResponseDTO> searchByRequester(ApprovalSearchRequestDTO request);

	// 팀장
	List<ApprovalSearchResponseDTO> searchByApprover(ApprovalSearchRequestDTO request);
}