package com.clover.salad.notice.query.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.notice.query.dto.CheckInfoDTO;
import com.clover.salad.notice.query.dto.NoticeDetailDTO;
import com.clover.salad.notice.query.dto.NoticeListDTO;

@Mapper
public interface NoticeMapper {
	List<NoticeListDTO> findNoticeList(
		@Param("employeeId") int employeeId,
		@Param("level") String label,
		@Param("departmentId") int departmentId);

	NoticeDetailDTO getNoticeDetail(Map<String, Object> params);

	List<CheckInfoDTO> findTargetEmployeesOfNotice(int noticeId);
}
