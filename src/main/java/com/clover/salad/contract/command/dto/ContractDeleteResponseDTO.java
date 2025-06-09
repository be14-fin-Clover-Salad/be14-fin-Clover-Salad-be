package com.clover.salad.contract.command.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ContractDeleteResponseDTO {
	private int id;

	@JsonProperty("isDeleted")
	private boolean isDeleted;

}
