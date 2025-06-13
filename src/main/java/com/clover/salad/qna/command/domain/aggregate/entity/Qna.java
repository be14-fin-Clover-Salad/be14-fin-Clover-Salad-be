package com.clover.salad.qna.command.domain.aggregate.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="qna")
@Getter
@Setter
public class Qna {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name ="created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "answer_status", nullable = false)
	private String answerStatus = "대기";

	@Column(name = "answer_content", nullable = true)
	private String answerContent;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@Column(name = "employee_id", nullable = false)
	private int employeeId;
}
