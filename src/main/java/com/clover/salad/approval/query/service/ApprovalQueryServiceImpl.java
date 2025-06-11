package com.clover.salad.approval.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;
import com.clover.salad.approval.query.mapper.ApprovalMapper;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.security.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalQueryServiceImpl implements ApprovalQueryService {

	private final ApprovalMapper approvalMapper;
	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public ApprovalQueryServiceImpl(ApprovalMapper approvalMapper,
		EmployeeQueryService employeeQueryService) {
		this.approvalMapper = approvalMapper;
		this.employeeQueryService = employeeQueryService;
	}

	@Override
	public List<ApprovalSearchResponseDTO> searchApprovals(ApprovalSearchRequestDTO request) {
		int employeeId = SecurityUtil.getEmployeeId();

		if (SecurityUtil.hasRole("ROLE_ADMIN")) {
			return approvalMapper.searchApprovals(request);
		}

		String employeeName = employeeQueryService.findNameById(employeeId);

		if (SecurityUtil.hasRole("ROLE_MANAGER")) {
			request.setAprvId(employeeId);
			request.setAprvName(employeeName);
			return approvalMapper.searchByApprover(request);
		}

		if (SecurityUtil.hasRole("ROLE_MEMBER")) {
			request.setReqId(employeeId);
			request.setReqName(employeeName);
			return approvalMapper.searchByRequester(request);
		}
		throw new AccessDeniedException("유효한 권한이 없습니다.");
	}
}