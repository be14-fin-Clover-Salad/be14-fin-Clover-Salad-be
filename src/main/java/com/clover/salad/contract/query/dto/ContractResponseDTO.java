package com.clover.salad.contract.query.dto;

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


	private String productName;
	private String serialNumber;
	private int quantity;
	private int rentalCost;
}
