package com.clover.salad.notice.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.notice.query.dto.NoticeDTO;

@Mapper
public interface NoticeMapper {
	List<NoticeDTO> findNoticeList();

	NoticeDTO findNoticeDetail(int noticeId);
}
