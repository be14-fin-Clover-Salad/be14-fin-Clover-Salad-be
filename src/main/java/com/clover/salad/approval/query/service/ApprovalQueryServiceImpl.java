package com.clover.salad.approval.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;
import com.clover.salad.approval.query.mapper.ApprovalMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalQueryServiceImpl implements ApprovalQueryService {

	private final ApprovalMapper approvalMapper;

	@Autowired
	public ApprovalQueryServiceImpl(ApprovalMapper approvalMapper) {
		this.approvalMapper = approvalMapper;
	}

	@Override
	public List<ApprovalSearchResponseDTO> searchApprovals(ApprovalSearchRequestDTO request) {
		return approvalMapper.searchApprovals(request);
	}
}