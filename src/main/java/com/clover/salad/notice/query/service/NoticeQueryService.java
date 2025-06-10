package com.clover.salad.notice.query.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.clover.salad.notice.query.dto.NoticeDetailDTO;
import com.clover.salad.notice.query.dto.NoticeListDTO;

@Service
public interface NoticeQueryService {
	List<NoticeListDTO> findNoticeList(int employeeId);

	NoticeDetailDTO getNoticeDetail(int noticeId, int employeeId);
}
