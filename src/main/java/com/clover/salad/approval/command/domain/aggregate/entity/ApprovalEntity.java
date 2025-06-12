package com.clover.salad.approval.command.domain.aggregate.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clover.salad.approval.command.domain.aggregate.converter.ApprovalStateConverter;
import com.clover.salad.approval.command.domain.aggregate.enums.ApprovalState;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "approval")
public class ApprovalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "code")
	private String code;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "req_date")
	private LocalDateTime reqDate;

	@Column(name = "aprv_date")
	private LocalDateTime aprvDate;

	@Column(name = "state")
	@Convert(converter = ApprovalStateConverter.class)
	private ApprovalState state;

	@Column(name = "comment")
	private String comment;

	@Column(name = "req_id")
	private int reqId;

	@Column(name = "aprv_id")
	private Integer aprvId;

	@Column(name = "contract_id")
	private int contractId;

	/* 설명. 결재 승인, 반려 */
	public void approve(String comment, LocalDateTime now) {
		this.state = ApprovalState.APPROVED;
		this.aprvDate = now;
		this.comment = comment;
	}

	public void reject(String comment, LocalDateTime now) {
		this.state = ApprovalState.REJECTED;
		this.aprvDate = now;
		this.comment = comment;
	}
}
