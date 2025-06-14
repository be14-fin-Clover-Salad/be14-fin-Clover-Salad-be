package com.clover.salad.notice.query.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;
import com.clover.salad.notice.query.dto.CheckInfoDTO;
import com.clover.salad.notice.query.dto.NoticeDetailDTO;
import com.clover.salad.notice.query.dto.NoticeListDTO;
import com.clover.salad.notice.query.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeQueryServiceImpl implements NoticeQueryService {

	private final NoticeMapper noticeMapper;
	private final EmployeeMapper employeeMapper;

	@Autowired
	public NoticeQueryServiceImpl(NoticeMapper noticeMapper, EmployeeMapper employeeMapper) {
		this.noticeMapper = noticeMapper;
		this.employeeMapper = employeeMapper;
	}

	@Override
	public List<NoticeListDTO> findNoticeList(int employeeId) {

		EmployeeQueryDTO employee = employeeMapper.findEmployeeById(employeeId);

		return noticeMapper.findNoticeList(
			employeeId,
			employee.getLevel(),
			employee.getDepartment().getId());
	}

	@Override
	public NoticeDetailDTO getNoticeDetail(int noticeId, int employeeId) {

		EmployeeQueryDTO employee = employeeMapper.findEmployeeById(employeeId);

		Map<String, Object> params = new HashMap<>();
		params.put("noticeId", noticeId);
		params.put("employeeId", employeeId);
		params.put("level", employee.getLevel());

		NoticeDetailDTO detail = noticeMapper.getNoticeDetail(params);
		if (detail == null) {
			return null;
		}

		List<CheckInfoDTO> checkList = noticeMapper.findTargetEmployeesOfNotice(noticeId);
		detail.setCheckList(checkList);

		return detail;
	}
}
