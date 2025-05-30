package com.clover.salad.notice.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.notice.query.dto.NoticeDTO;

@Service
public interface NoticeQueryService {
	List<NoticeDTO> findNoticeList();

	NoticeDTO findNoticeDeatil(int noticeId);
}
