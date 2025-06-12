package com.clover.salad.notice.command.application.service;

import org.springframework.stereotype.Service;

import com.clover.salad.notice.command.application.dto.NoticeCreateRequest;

@Service
public class NoticeCommandServiceImpl implements NoticeCommandService {
	@Override
	public void createNotice(NoticeCreateRequest request, int writerId) {
		
	}
}
