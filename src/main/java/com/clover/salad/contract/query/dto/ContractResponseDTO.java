package com.clover.salad.contract.query.dto;

import java.util.List;

import com.clover.salad.approval.query.dto.ApprovalDetailDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContractResponseDTO {

	private int id;
	private String renameFile;
	private String code;
	private int amount;

	private String customerName;
	private String customerAddress;
	private String customerPhone;

	private String employeeName;

	private String aprvTitle;
	private String aprvContent;
	private String aprvState;
	private int reqId;
	private int aprvId;

	private List<ProductDTO> productList;

	private List<ApprovalDetailDTO> approvalList;


	private String thumbnailUrl; // 썸네일 이미지 URL
	private String fileUrl;      // 실제 PDF 파일 URL
}
