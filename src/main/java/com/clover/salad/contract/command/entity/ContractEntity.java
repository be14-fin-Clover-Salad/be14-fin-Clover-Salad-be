package com.clover.salad.contract.command.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractEntity {
	/*
	 * 계약서에서 파싱하여 가져오는 기본 정보
	 */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String code;
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
	/*
	 * 데이터를 계약 테이블에 저장할때 필요한 추가 속성.
	 * Builder 패턴을 통해 기본값 설정 혹은 서비스 계층에서 처리
	 */

	private LocalDateTime createdAt;


	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	private String etc;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "document_origin_id", nullable = false)
	private DocumentOrigin documentOrigin;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private EmployeeEntity employee;

}