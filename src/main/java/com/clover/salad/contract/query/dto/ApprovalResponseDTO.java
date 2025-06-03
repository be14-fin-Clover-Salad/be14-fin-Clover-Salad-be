package com.clover.salad.contract.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalResponseDTO {
	private String title;
	private String content;
	private String state;
	private int reqId;
	private int aprvId;
}
