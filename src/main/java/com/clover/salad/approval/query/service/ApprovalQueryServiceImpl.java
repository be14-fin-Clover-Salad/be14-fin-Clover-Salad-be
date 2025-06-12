package com.clover.salad.approval.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.clover.salad.approval.query.dto.ApprovalDetailDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;
import com.clover.salad.approval.query.mapper.ApprovalMapper;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.security.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;
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

	@Override
	public ApprovalDetailDTO getApprovalDetailById(int id) {
		ApprovalDetailDTO dto = approvalMapper.findApprovalDetailById(id);

		if (dto == null) {
			throw new EntityNotFoundException("해당 결재를 찾을 수 없습니다.");
		}

		int currentEmployeeId = SecurityUtil.getEmployeeId();

		// 권한별 접근 제어
		if (SecurityUtil.hasRole("ROLE_ADMIN")) {
			return dto;
		}

		if (SecurityUtil.hasRole("ROLE_MANAGER")) {
			if (!dto.getAprvId().equals(currentEmployeeId)) {
				throw new AccessDeniedException("팀장은 자신에게 요청된 결재만 조회할 수 있습니다.");
			}
			return dto;
		}

		if (SecurityUtil.hasRole("ROLE_MEMBER")) {
			if (!dto.getReqId().equals(currentEmployeeId)) {
				throw new AccessDeniedException("사원은 자신이 요청한 결재만 조회할 수 있습니다.");
			}
			return dto;
		}

		throw new AccessDeniedException("유효한 권한이 없습니다.");
	}
}
