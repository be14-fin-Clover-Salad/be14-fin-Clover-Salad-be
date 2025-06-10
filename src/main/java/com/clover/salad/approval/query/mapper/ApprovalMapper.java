package com.clover.salad.approval.query.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;

@Mapper
public interface ApprovalMapper {
	List<ApprovalSearchResponseDTO> searchApprovals(ApprovalSearchRequestDTO request);
}