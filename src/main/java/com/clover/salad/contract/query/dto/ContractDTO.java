package com.clover.salad.contract.query.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDTO {
	private int id;
	private String code;
	private LocalDateTime createdAt;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	private int amount;
	private String bankName;
	private String bankAccount;
	private int paymentDay;
	private String depositOwner;
	private String relationship;
	private String paymentEmail;
	@JsonProperty("is_deleted")
	private boolean isDeleted;
	private String etc;

	private String employeeName;  // 담당 영업사원 이름
	private String customerName;  // 고객 이름
	private String productNames;  // 계약된 상품들 ("청소기 외 2개" 형식)

	private int customerId;

}
