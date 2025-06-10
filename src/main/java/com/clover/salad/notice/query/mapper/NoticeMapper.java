package com.clover.salad.notice.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.notice.query.dto.NoticeQueryDTO;

@Mapper
public interface NoticeMapper {
	List<NoticeQueryDTO> findNoticeList(
		@Param("employeeId") int employeeId,
		@Param("level") String label,
		@Param("departmentId") int departmentId);

	NoticeQueryDTO findNoticeDetail(int noticeId);

}
