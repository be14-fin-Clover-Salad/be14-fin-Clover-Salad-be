package com.clover.salad.sales.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "sales")
public class SalesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "sales_date")
	private LocalDate salesDate;

	/* 설명. 3단 조인이 일어아야하는 비효율을 해소하고자 관계 연결하지 않음 */
	@Column(name = "department")
	private String department;

	@Column(name = "employee_name")
	private String employeeName;

	@Column(name = "amount")
	private int amount;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	@Column(name = "contract_id")
	private int contractId;

	public void delete() {
		this.isDeleted = true;
	}
}
