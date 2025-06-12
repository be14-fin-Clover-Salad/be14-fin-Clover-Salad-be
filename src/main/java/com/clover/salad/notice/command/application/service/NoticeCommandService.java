package com.clover.salad.notice.command.application.service;

import com.clover.salad.notice.command.application.dto.NoticeCreateRequest;

public interface NoticeCommandService {
	void createNotice(NoticeCreateRequest request, int writerId);
}
