package com.clover.salad.notice.command.domain.aggregate.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="employee_notice")
@Getter
@Setter
public class EmployeeNotice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "is_checked", nullable = false)
	private boolean isChecked = false;

	@Column(name = "employee_id", nullable = false)
	private int employeeId;

	@Column(name = "notice_id", nullable = false)
	private int noticeId;
}
