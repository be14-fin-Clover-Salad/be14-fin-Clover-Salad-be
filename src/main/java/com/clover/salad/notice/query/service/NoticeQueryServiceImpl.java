package com.clover.salad.notice.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.notice.query.dto.NoticeListDTO;
import com.clover.salad.notice.query.mapper.NoticeMapper;

@Service
public class NoticeQueryServiceImpl implements NoticeQueryService {

	private NoticeMapper noticeMapper;

	@Autowired
	public NoticeQueryServiceImpl(NoticeMapper noticeMapper) {
		this.noticeMapper = noticeMapper;
	}

	@Override
	public List<NoticeListDTO> findNoticeList() {
		return noticeMapper.findNoticeList();
	}
}
