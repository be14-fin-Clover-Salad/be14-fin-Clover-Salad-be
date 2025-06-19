package com.clover.salad.qna.command.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.common.exception.BadRequestException;
import com.clover.salad.common.exception.NotFoundException;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;
import com.clover.salad.notification.command.application.service.NotificationCommandService;
import com.clover.salad.notification.command.domain.aggregate.enums.NotificationType;
import com.clover.salad.notification.command.domain.repository.NotificationRepository;
import com.clover.salad.qna.command.application.dto.QnaAnswerRequest;
import com.clover.salad.qna.command.application.dto.QnaCreateRequest;
import com.clover.salad.qna.command.domain.aggregate.entity.Qna;
import com.clover.salad.qna.command.domain.repository.QnaRepository;

@Service
public class QnaCommandServiceImpl implements QnaCommandService {

	private final QnaRepository qnaRepository;
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	private final NotificationCommandService notificationCommandService;

	@Autowired
	public QnaCommandServiceImpl(QnaRepository qnaRepository,
		EmployeeRepository employeeRepository,
		EmployeeMapper employeeMapper,
		NotificationCommandService notificationCommandService
	) {
		this.qnaRepository = qnaRepository;
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
		this.notificationCommandService = notificationCommandService;
	}

	@Override
	@Transactional
	public void createQna(QnaCreateRequest request, int writerId) {

		if(request.getTitle() == null || request.getTitle().trim().isEmpty()) {
			throw new BadRequestException("제목은 필수입니다.");
		}

		if(request.getContent() == null || request.getContent().trim().isEmpty()) {
			throw new BadRequestException("내용은 필수입니다.");
		}

		if(!employeeRepository.existsById(writerId)) {
			throw new NotFoundException("작성자 정보가 존재하지 않습니다.");
		}

		Qna qna = new Qna();
		qna.setTitle(request.getTitle());
		qna.setContent(request.getContent());
		qna.setCreatedAt(LocalDateTime.now());
		qna.setEmployeeId(writerId);
		qnaRepository.save(qna);

		// 알림 생성
		List<Integer> adminIds = employeeMapper.findAdminIds();

		for (Integer adminId : adminIds) {
			notificationCommandService.createNotification(NotificationCreateDTO.builder()
				.type(NotificationType.QNA)
				.content("새로운 문의사항이 등록되었습니다.")
				.url("/support/qna/" + qna.getId())
				.employeeId(adminId)
				.build());
		}
	}

	@Override
	@Transactional
	public void deleteQna(int qnaId, int writerId) {
		Qna qna = qnaRepository.findById(qnaId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문의사항입니다."));

		EmployeeEntity employee = employeeRepository.findById(writerId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		boolean isAdmin = employee.isAdmin();

		if(!isAdmin){
			if(qna.getEmployeeId() != writerId) {
				throw new SecurityException("작성자 또는 관리자만 삭제할 수 있습니다.");
			}
			if(!"대기".equals(qna.getAnswerStatus())){
				throw new BadRequestException("답변 완료된 문의사항은 삭제할 수 없습니다.");
			}
		}

		if(qna.isDeleted()){
			throw new IllegalStateException("이미 삭제된 문의입니다.");
		}

		qna.setDeleted(true);

		qnaRepository.save(qna);
	}

	@Override
	@Transactional
	public void answerQna(int qnaId, QnaAnswerRequest request, int writerId) {
		Qna qna = qnaRepository.findById(qnaId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문의사항입니다."));

		EmployeeEntity employee = employeeRepository.findById(writerId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		if(qna.isDeleted()){
			throw new IllegalStateException("삭제된 문의에는 답변할 수 없습니다.");
		}

		boolean isAdmin = employee.isAdmin();

		if(!isAdmin) {
			throw new SecurityException("관리자만 작성할 수 있습니다.");
		}

		if("완료".equals(qna.getAnswerStatus())){
			throw new BadRequestException("이미 답변이 완료된 문의사항입니다.");
		}

		qna.setAnswerContent(request.getAnswerContent());
		qna.setAnswerStatus("완료");

		qnaRepository.save(qna);

		// 알림 생성
		notificationCommandService.createNotification(NotificationCreateDTO.builder()
			.type(NotificationType.QNA)
			.content("문의하신 내용에 답변이 등록되었습니다.")
			.url("/support/qna/" + qna.getId())
			.employeeId(qna.getEmployeeId())
			.build());
	}

	@Override
	@Transactional
	public void updateAnswer(int qnaId, QnaAnswerRequest request, int writerId) {
		Qna qna = qnaRepository.findById(qnaId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문의사항입니다."));

		EmployeeEntity employee = employeeRepository.findById(writerId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		if(qna.isDeleted()) {
			throw new IllegalStateException("삭제된 문의에는 답변할 수 없습니다.");
		}

		if("대기".equals(qna.getAnswerStatus())) {
			throw new IllegalStateException("답변이 달리지 않는 문의는 수정할 수 없습니다.");
		}

		boolean isAdmin = employee.isAdmin();

		if(!isAdmin) {
			throw new SecurityException("관리자만 수정할 수 있습니다.");
		}

		qna.setAnswerContent(request.getAnswerContent());

		qnaRepository.save(qna);
	}

}
