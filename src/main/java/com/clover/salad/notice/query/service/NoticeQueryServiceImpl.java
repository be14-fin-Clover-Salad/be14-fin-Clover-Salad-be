package com.clover.salad.notice.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.notice.query.dto.NoticeDTO;
import com.clover.salad.notice.query.mapper.NoticeMapper;

@Service
public class NoticeQueryServiceImpl implements NoticeQueryService {

	private NoticeMapper noticeMapper;

	@Autowired
	public NoticeQueryServiceImpl(NoticeMapper noticeMapper) {
		this.noticeMapper = noticeMapper;
	}

	@Override
	public List<NoticeDTO> findNoticeList() {
		return noticeMapper.findNoticeList();
	}

	@Override
	public NoticeDTO findNoticeDeatil(int noticeId) {
		return noticeMapper.findNoticeDetail(noticeId);
	}
}
