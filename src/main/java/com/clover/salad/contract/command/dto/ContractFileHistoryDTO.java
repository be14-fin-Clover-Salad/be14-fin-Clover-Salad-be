package com.clover.salad.contract.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import com.clover.salad.contract.command.entity.ContractFileHistory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ContractFileHistoryDTO {

	private int id;
	private int contractId;
	private int version;
	private String originFile;
	private String renamedFile;
	private LocalDateTime uploadedAt;
	private Integer uploaderId;
	private String note;

	public static ContractFileHistoryDTO fromEntity(ContractFileHistory entity) {
		return ContractFileHistoryDTO.builder()
			.id(entity.getId())
			.contractId(entity.getContract().getId())
			.version(entity.getVersion())
			.originFile(entity.getOriginFile())
			.renamedFile(entity.getRenamedFile())
			.uploadedAt(entity.getUploadedAt())
			.uploaderId(entity.getUploaderId())
			.note(entity.getNote())
			.build();
	}
}
