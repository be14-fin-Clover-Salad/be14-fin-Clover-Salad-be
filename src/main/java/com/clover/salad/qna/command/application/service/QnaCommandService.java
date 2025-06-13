package com.clover.salad.qna.command.application.service;

import org.apache.coyote.BadRequestException;
import org.apache.ibatis.javassist.NotFoundException;

import com.clover.salad.qna.command.application.dto.QnaCreateRequest;

public interface QnaCommandService {
	void createQna(QnaCreateRequest request, int writerId);
}
