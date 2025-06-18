package com.clover.salad.approval.query.service;

import java.util.List;

import com.clover.salad.approval.query.dto.ApprovalDetailDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;

public interface ApprovalQueryService {
	List<ApprovalSearchResponseDTO> searchApprovals(ApprovalSearchRequestDTO request);

	ApprovalDetailDTO getApprovalDetailById(int id);
}
