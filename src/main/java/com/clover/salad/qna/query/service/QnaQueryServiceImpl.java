package com.clover.salad.qna.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.qna.query.dto.QnaDetailDTO;
import com.clover.salad.qna.query.dto.QnaListDTO;
import com.clover.salad.qna.query.mapper.QnaMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QnaQueryServiceImpl implements QnaQueryService {

	private final QnaMapper qnaMapper;
	private final EmployeeMapper employeeMapper;

	@Autowired
	public QnaQueryServiceImpl(QnaMapper qnaMapper, EmployeeMapper employeeMapper) {
		this.qnaMapper = qnaMapper;
		this.employeeMapper = employeeMapper;
	}

	@Override
	public List<QnaListDTO> findQnaList(int employeeId) {
		EmployeeQueryDTO employee = employeeMapper.findEmployeeById(employeeId);

		return qnaMapper.findQnaList(employeeId,employee.getLevel());
	}

	@Override
	public QnaDetailDTO findQnaDetail(int qnaId, int employeeId) {
		EmployeeQueryDTO employee = employeeMapper.findEmployeeById(employeeId);

		QnaDetailDTO dto = qnaMapper.findQnaDetail(qnaId, employeeId, employee.getLevel());

		if (dto == null) {
			throw new RuntimeException("조회 권한이 없거나 존재하지 않는 문의입니다.");
		}

		return dto;
	}
}
