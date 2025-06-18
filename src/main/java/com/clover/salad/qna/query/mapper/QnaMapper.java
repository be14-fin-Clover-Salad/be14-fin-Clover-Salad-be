package com.clover.salad.qna.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.qna.query.dto.QnaDetailDTO;
import com.clover.salad.qna.query.dto.QnaListDTO;

@Mapper
public interface QnaMapper {
	List<QnaListDTO> findQnaList(int employeeId, String level);

	QnaDetailDTO findQnaDetail(int qnaId, int employeeId, String level);
}
