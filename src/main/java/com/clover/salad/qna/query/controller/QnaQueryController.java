package com.clover.salad.qna.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.qna.query.dto.QnaDetailDTO;
import com.clover.salad.qna.query.dto.QnaListDTO;
import com.clover.salad.qna.query.service.QnaQueryService;
import com.clover.salad.security.SecurityUtil;

@RequestMapping("/support")
@RestController
public class QnaQueryController {

	private final QnaQueryService qnaQueryService;

	@Autowired
	public QnaQueryController(QnaQueryService qnaQueryService) {
		this.qnaQueryService = qnaQueryService;
	}

	@GetMapping("/qna")
	public ResponseEntity<List<QnaListDTO>> findQnaList() {
		int employeeId = SecurityUtil.getEmployeeId();

		return ResponseEntity.ok(qnaQueryService.findQnaList(employeeId));
	}

	@GetMapping("/qna/{qnaId}")
	public ResponseEntity<QnaDetailDTO> findQnaDetail(@PathVariable("qnaId") int qnaId) {
		int employeeId = SecurityUtil.getEmployeeId();

		return ResponseEntity.ok(qnaQueryService.findQnaDetail(qnaId,employeeId));
	}
}
