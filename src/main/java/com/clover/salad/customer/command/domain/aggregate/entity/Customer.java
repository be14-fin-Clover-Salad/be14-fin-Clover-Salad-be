package com.clover.salad.customer.command.domain.aggregate.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 고객 엔티티 - 등록/수정/삭제는 JPA 기반 Command 처리
 */
@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(length = 20)
	private String birthdate;

	@Column(nullable = false, length = 11)
	private String phone;

	@Column(unique = true, length = 255)
	private String email;

	@Column(length = 255)
	private String address;

	@Column(name = "register_at", nullable = false)
	private LocalDate registerAt;

	@JsonProperty("isDeleted")
	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	@Column(nullable = false, length = 20)
	private String type;

	@Column(length = 20)
	private String etc;

	/**
	 * 변경된 값이 있을 경우만 갱신되도록 처리
	 */
	public void update(Customer updated) {
		this.name = updated.name != null ? updated.name : this.name;
		this.birthdate = updated.birthdate != null ? updated.birthdate : this.birthdate;
		this.phone = updated.phone != null ? updated.phone : this.phone;
		this.address = updated.address != null ? updated.address : this.address;
		this.email = updated.email != null ? updated.email : this.email;
		this.type = updated.type != null ? updated.type : this.type;
		this.etc = updated.etc != null ? updated.etc : this.etc;
	}

	/**
	 * 소프트 삭제 처리
	 */
	public void softDelete() {
		this.isDeleted = true;
	}
}
