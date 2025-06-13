package com.clover.salad.qna.command.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.qna.command.domain.repository.QnaRepository;

@Service
public class QnaCommandServiceImpl implements QnaCommandService {

	private final QnaRepository qnaRepository;

	@Autowired
	public QnaCommandServiceImpl(QnaRepository qnaRepository) {
		this.qnaRepository = qnaRepository;
	}
}
