package com.clover.salad.approval.command.application.service;

import com.clover.salad.approval.command.application.dto.ApprovalRequestDTO;
import com.clover.salad.approval.command.domain.aggregate.entity.ApprovalEntity;
import com.clover.salad.approval.command.domain.aggregate.enums.ApprovalState;
import com.clover.salad.approval.command.domain.repository.ApprovalRepository;
import com.clover.salad.approval.query.mapper.ApprovalMapper;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.security.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ApprovalCommandServiceImpl implements ApprovalCommandService {

	private final ApprovalRepository approvalRepository;
	private final EmployeeMapper employeeMapper;
	private final ApprovalMapper approvalMapper;

	@Autowired
	public ApprovalCommandServiceImpl(ApprovalRepository approvalRepository,
		EmployeeMapper employeeMapper,
		ApprovalMapper approvalMapper) {
		this.approvalRepository = approvalRepository;
		this.employeeMapper = employeeMapper;
		this.approvalMapper = approvalMapper;
	}

	@Override
	public int requestApproval(ApprovalRequestDTO dto) {
		int requesterId = SecurityUtil.getEmployeeId();

		// 기본 결재 엔티티 빌더 시작
		ApprovalEntity.ApprovalEntityBuilder builder = ApprovalEntity.builder()
			.code(generateCode())
			.title(dto.getTitle())
			.content(dto.getContent())
			.reqDate(LocalDateTime.now())
			.reqId(requesterId)
			.state(ApprovalState.REQUESTED)
			.contractId(dto.getContractId());

		// 요청자가 ROLE_MEMBER인 경우: 팀장 ID 조회
		if (SecurityUtil.hasRole("ROLE_MEMBER")) {
			Integer deptId = employeeMapper.findDepartmentIdByEmployeeId(requesterId);
			if (deptId == null) {
				throw new EntityNotFoundException("요청자의 부서를 찾을 수 없습니다.");
			}

			Integer managerId = employeeMapper.findManagerIdByDeptId(deptId);
			if (managerId == null) {
				throw new IllegalStateException("해당 부서에 팀장이 존재하지 않습니다.");
			}

			builder.aprvId(managerId);
		}

		ApprovalEntity approval = builder.build();
		return approvalRepository.save(approval).getId();
	}

	@Override
	public String getApprovalCodeById(int approvalId) {
		return approvalRepository.findById(approvalId)
			.orElseThrow(() -> new EntityNotFoundException("결재 정보를 찾을 수 없습니다."))
			.getCode();
	}

	/* 설명. 결재 코드 작성 */
	private String generateCode() {
		LocalDate now = LocalDate.now();
		String yearMonth = String.format("%02d%02d", now.getYear() % 100, now.getMonthValue());
		String prefix = "A-" + yearMonth;

		String lastCode = approvalMapper.findLastCodeByPrefix(prefix);

		int nextSeq = 1;
		if (lastCode != null && lastCode.length() == 11) {
			String lastSeqStr = lastCode.substring(7);
			try {
				nextSeq = Integer.parseInt(lastSeqStr) + 1;
			} catch (NumberFormatException e) {
				throw new IllegalStateException("잘못된 결재 코드 형식: " + lastCode);
			}
		}

		return String.format("%s%04d", prefix, nextSeq);
	}
}
