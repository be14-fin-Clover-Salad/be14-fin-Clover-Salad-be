package com.clover.salad.notice.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.notice.query.dto.NoticeQueryDTO;

@Service
public interface NoticeQueryService {
	List<NoticeQueryDTO> findNoticeList(int employeeId);

	NoticeQueryDTO findNoticeDeatil(int noticeId);
}
