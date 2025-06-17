package com.clover.salad.qna.command.application.service;

import com.clover.salad.qna.command.application.dto.QnaAnswerRequest;
import com.clover.salad.qna.command.application.dto.QnaCreateRequest;

public interface QnaCommandService {
	void createQna(QnaCreateRequest request, int writerId);

	void deleteQna(int qnaId, int writerId);

	void answerQna(int qnaId, QnaAnswerRequest request, int writerId);

	void updateAnswer(int qnaId, QnaAnswerRequest request, int writerId);
}
