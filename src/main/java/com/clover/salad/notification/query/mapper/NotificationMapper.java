package com.clover.salad.notification.query.mapper;

import com.clover.salad.notification.query.dto.NotificationDropdownResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
	List<NotificationDropdownResponseDTO> findTop5UnreadByEmployeeId(@Param("employeeId") int employeeId);
}
