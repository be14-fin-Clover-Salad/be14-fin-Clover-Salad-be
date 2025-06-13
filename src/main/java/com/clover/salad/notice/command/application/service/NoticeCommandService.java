package com.clover.salad.notice.command.application.service;

import com.clover.salad.notice.command.application.dto.NoticeCreateRequest;
import com.clover.salad.notice.command.application.dto.NoticeUpdateRequest;

public interface NoticeCommandService {
	void createNotice(NoticeCreateRequest request, int writerId);

	void updateNotice(int noticeId, NoticeUpdateRequest request, int writerId);

	void deleteNotice(int noticeId, int writerId);

	void checkNotice(int noticeId, int writerId);
}
