package com.clover.salad.notice.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.notice.query.dto.NoticeQueryDTO;
import com.clover.salad.notice.query.mapper.NoticeMapper;
import com.clover.salad.security.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeQueryServiceImpl implements NoticeQueryService {

	private NoticeMapper noticeMapper;
	private EmployeeMapper employeeMapper;

	@Autowired
	public NoticeQueryServiceImpl(NoticeMapper noticeMapper, EmployeeMapper employeeMapper) {
		this.noticeMapper = noticeMapper;
		this.employeeMapper = employeeMapper;
	}

	@Override
	public List<NoticeQueryDTO> findNoticeList(int employeeId) {

		EmployeeQueryDTO employee = employeeMapper.findEmployeeById(employeeId);

		return noticeMapper.findNoticeList(
			employeeId,
			employee.getLevel(),
			employee.getDepartment().getId());
	}

	@Override
	public NoticeQueryDTO findNoticeDeatil(int noticeId) {
		return noticeMapper.findNoticeDetail(noticeId);
	}
}
