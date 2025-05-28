package com.clover.salad.contract.command.entity;

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
	private Long id;

	private String contractNumber;
	private String productName;
	private String monthlyFee;
	private String totalFee;
	private String rentalPeriod;
	private String paymentMethod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private CustomerEntity customer;
}