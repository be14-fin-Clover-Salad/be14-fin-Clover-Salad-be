package com.clover.salad.contract.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractUploadResponseDTO {
	private int contractId;
	private String message;

	public ContractUploadResponseDTO(Integer id, String message) {
		this.contractId = (id != null) ? id : -1; // null 처리
		this.message = message;
	}
}