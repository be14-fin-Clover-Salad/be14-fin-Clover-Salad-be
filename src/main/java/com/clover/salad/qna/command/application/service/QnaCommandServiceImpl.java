package com.clover.salad.qna.command.application.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.common.exception.BadRequestException;
import com.clover.salad.common.exception.NotFoundException;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.qna.command.application.dto.QnaCreateRequest;
import com.clover.salad.qna.command.domain.aggregate.entity.Qna;
import com.clover.salad.qna.command.domain.repository.QnaRepository;

@Service
public class QnaCommandServiceImpl implements QnaCommandService {

	private final QnaRepository qnaRepository;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public QnaCommandServiceImpl(QnaRepository qnaRepository, EmployeeRepository employeeRepository) {
		this.qnaRepository = qnaRepository;
		this.employeeRepository = employeeRepository;
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
	}
}
