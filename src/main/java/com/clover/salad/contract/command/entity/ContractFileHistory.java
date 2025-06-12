package com.clover.salad.contract.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract_file_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractFileHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false)
	private ContractEntity contract;

	@ManyToOne
	@JoinColumn(name = "replaced_contract_id")
	private ContractEntity replacedContract;


	private int version;

	@Column(name = "origin_file", nullable = false)
	private String originFile;

	@Column(name = "renamed_file", nullable = false)
	private String renamedFile;

	@Column(name = "uploaded_at", nullable = false)
	private LocalDateTime uploadedAt;

	private Integer uploaderId;

	private String note;
}
