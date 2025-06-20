package com.clover.salad.contract.command.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clover.salad.contract.common.ContractStatus;
import com.clover.salad.contract.common.ContractStatusConverter;
import com.clover.salad.contract.common.RelationshipType;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;

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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	private LocalDate startDate;
	private LocalDate endDate;

	@Column(name = "status", nullable = false, length = 20)
	@Convert(converter = ContractStatusConverter.class)
	private ContractStatus status;

	private int amount;

	private String bankName;
	private String bankAccount;
	private int paymentDay;
	private String depositOwner;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RelationshipType relationship;

	private String paymentEmail;

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

	public void changeStatus(ContractStatus status) {
		this.status = status;
	}
}
