package com.clover.salad.approval.command.application.service;

import com.clover.salad.approval.command.application.dto.ApprovalDecisionDTO;
import com.clover.salad.approval.command.application.dto.ApprovalRequestDTO;
import com.clover.salad.approval.command.domain.aggregate.entity.ApprovalEntity;
import com.clover.salad.approval.command.domain.aggregate.enums.ApprovalState;
import com.clover.salad.approval.command.domain.repository.ApprovalRepository;
import com.clover.salad.approval.query.dto.ApprovalExistenceCheckDTO;
import com.clover.salad.approval.query.mapper.ApprovalMapper;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.repository.ContractRepository;
import com.clover.salad.contract.common.ContractStatus;
import com.clover.salad.contract.query.mapper.ContractMapper;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;
import com.clover.salad.notification.command.application.service.NotificationCommandService;
import com.clover.salad.notification.command.domain.aggregate.enums.NotificationType;
import com.clover.salad.security.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ApprovalCommandServiceImpl implements ApprovalCommandService {

	private final ApprovalRepository approvalRepository;
	private final EmployeeMapper employeeMapper;
	private final ApprovalMapper approvalMapper;
	private final NotificationCommandService notificationCommandService;
	private final ContractMapper contractMapper;
	private final ContractRepository contractRepository;

	@Autowired
	public ApprovalCommandServiceImpl(ApprovalRepository approvalRepository,
		EmployeeMapper employeeMapper,
		ApprovalMapper approvalMapper,
		NotificationCommandService notificationCommandService,
		ContractMapper contractMapper,
		ContractRepository contractRepository
	) {
		this.approvalRepository = approvalRepository;
		this.employeeMapper = employeeMapper;
		this.approvalMapper = approvalMapper;
		this.notificationCommandService = notificationCommandService;
		this.contractMapper = contractMapper;
		this.contractRepository = contractRepository;
	}

	@Override
	public int requestApproval(ApprovalRequestDTO dto) {
		int requesterId = SecurityUtil.getEmployeeId();

		ContractEntity contract = contractRepository.findById(dto.getContractId())
			.orElseThrow(() -> new IllegalArgumentException("계약이 존재하지 않습니다. 계약 ID: " + dto.getContractId()));
		contract.changeStatus(ContractStatus.IN_PROGRESS);
		log.info("계약의 상태가 '결재중'으로 변경되었습니다.");

		/* 설명. 팀장은 결재 요청 불가능 */
		if (SecurityUtil.hasRole("ROLE_MANAGER")) {
			throw new AccessDeniedException("사원만 결재 요청이 가능합니다.");
		}

		/* 설명. 사원은 하나의 계약에 대해 결재를 요청했다면 반려 전까지는 같은 계약 건에 대해 새로운 결재를 생성할 수 없음 */
		// 하나의 계약 건에 대해 기존에 결재를 요청한 적이 있는지 체크
		ApprovalExistenceCheckDTO checkDTO = new ApprovalExistenceCheckDTO();
		checkDTO.setReqId(requesterId);
		checkDTO.setContractId(dto.getContractId());

		int existingCount = approvalMapper.countExistingApprovalRequest(checkDTO);

		if (existingCount > 0) {
			throw new IllegalStateException("결재가 반려된 경우가 아니라면 중복 결재 요청이 불가능합니다.");
		}

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

			ApprovalEntity approval = builder.build();
			int approvalId = approvalRepository.save(approval).getId();

			// 알림 생성
			String requesterName = employeeMapper.findNameById(requesterId);
			notificationCommandService.createNotification(NotificationCreateDTO.builder()
				.type(NotificationType.APPROVAL)
				.content(requesterName + " 님이 결재를 요청했습니다.")
				.url("/approval/" + approvalId)
				.employeeId(managerId)
				.build());

			return approvalId;
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
		String yearMonth = String.format("%02d%02d", now.getYear() % 100, now.getMonthValue()); // 2501
		String prefix = "A-" + yearMonth; // A-2501

		for (int attempt = 0; attempt < 5; attempt++) {
			String lastCode = approvalMapper.findLastCodeByPrefix(prefix); // e.g., A-2501-0003

			int nextSeq = 1;

			if (lastCode != null && lastCode.length() == 11) { // "A-2501-0003" = 11글자
				String lastSeqStr = lastCode.substring(8); // "0003"
				nextSeq = Integer.parseInt(lastSeqStr) + 1;
			}

			String newCode = String.format("%s-%04d", prefix, nextSeq); // "A-2501-0004"

			if (approvalMapper.countByCode(newCode) == 0) {
				return newCode;
			}
		}

		throw new IllegalStateException("결재 코드 생성 실패 - 중복 발생");
	}

	/* 설명. 팀장의 결재 로직 */
	@Override
	public void decideApproval(ApprovalDecisionDTO dto) {
		int approverId = SecurityUtil.getEmployeeId();

		if (!SecurityUtil.hasRole("ROLE_MANAGER")) {
			throw new AccessDeniedException("팀장만 결재 처리가 가능합니다.");
		}

		ApprovalEntity approval = approvalRepository.findById(dto.getApprovalId())
			.orElseThrow(() -> new EntityNotFoundException("결재 요청을 찾을 수 없습니다."));

		if (!approval.getAprvId().equals(approverId)) {
			throw new AccessDeniedException("본인에게 배정된 결재만 처리할 수 있습니다.");
		}

		// 결재를 승인, 반려하려면 요청이 있어야하니까
		if (!approval.getState().equals(ApprovalState.REQUESTED)) {
			throw new IllegalStateException("이미 처리된 결재입니다.");
		}

		// 대소문자 예외처리 위해 equalsIgnoreCase 사용 후 decision 값이 approve면 승인
		if ("APPROVE".equalsIgnoreCase(dto.getDecision())) {
			approval.approve(dto.getComment(), LocalDateTime.now());

			ContractEntity contract = contractRepository.findById(approval.getContractId())
				.orElseThrow(() -> new EntityNotFoundException("계약을 찾을 수 없습니다."));
			contract.changeStatus(ContractStatus.IN_CONTRACT);
			log.info("계약의 상태가 '계약중'으로 변경되었습니다.");

		// decision 값이 reject면 반려. 반려 사유 입력했는지 체크
		} else if ("REJECT".equalsIgnoreCase(dto.getDecision())) {
			if (dto.getComment() == null || dto.getComment().isBlank()) {
				throw new IllegalArgumentException("반려 시 사유를 입력해야 합니다.");
			}
			approval.reject(dto.getComment(), LocalDateTime.now());

		// 그외 접근은 예외처리
		} else {
			throw new IllegalArgumentException("결재 처리 방식이 유효하지 않습니다.");
		}

		approvalRepository.save(approval);

		// 알림 생성
		String decisionLabel = approval.getState().getLabel(); // 승인 or 반려
		notificationCommandService.createNotification(NotificationCreateDTO.builder()
			.type(NotificationType.APPROVAL)
			.content("결재가 " + decisionLabel + "되었습니다.")
			.url("/approval/" + approval.getId())
			.employeeId(approval.getReqId())
			.build());
	}
}
