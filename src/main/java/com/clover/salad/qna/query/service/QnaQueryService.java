package com.clover.salad.qna.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.qna.query.dto.QnaDetailDTO;
import com.clover.salad.qna.query.dto.QnaListDTO;

@Service
public interface QnaQueryService {
	List<QnaListDTO> findQnaList(int employeeId);

	QnaDetailDTO findQnaDetail(int qnaId, int employeeId);
}
