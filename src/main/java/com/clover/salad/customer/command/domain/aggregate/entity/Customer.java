package com.clover.salad.customer.command.domain.aggregate.entity;

import java.time.LocalDate;

import com.clover.salad.common.exception.CustomersException.InvalidCustomerDataException;
import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerTypeConverter;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

	@Column(length = 255)
	private String email;

	@Column(length = 255)
	private String address;

	@JsonProperty("registerAt")
	@Column(name = "register_at", nullable = false)
	private LocalDate registerAt;

	@JsonProperty("isDeleted")
	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	@Convert(converter = CustomerTypeConverter.class)
	@Column(name = "type", nullable = false, length = 20)
	private CustomerType type;

	@Column(length = 20)
	private String etc;

	/** JPA 영속화 전 등록일 자동 설정 */
	@PrePersist
	public void setRegisterAtIfNull() {
		if (this.registerAt == null) {
			this.registerAt = LocalDate.now();
		}
	}

	/** 변경된 값이 있을 경우만 업데이트 */
	public void update(Customer updated) {
		if (this.phone == null && updated.phone == null) {
			throw new InvalidCustomerDataException("연락처는 필수입니다.");
		}

		this.name = updated.name != null ? updated.name : this.name;
		this.birthdate = updated.birthdate != null ? updated.birthdate : this.birthdate;
		this.phone = updated.phone != null ? updated.phone : this.phone;
		this.address = updated.address != null ? updated.address : this.address;
		this.email = updated.email != null ? updated.email : this.email;
		this.type = updated.type != null ? updated.type : this.type;
		this.etc = updated.etc != null ? updated.etc : this.etc;
	}

	/** 정책에 따라 수정 허용된 필드만 업데이트 */
	public void updateFromRequest(CustomerCreateRequest request, CustomerType resolvedType) {
		if (request.getAddress() != null) {
			this.address = request.getAddress();
		}
		if (request.getEmail() != null) {
			this.email = request.getEmail();
		}
		this.type = resolvedType; // 외부 로직에서 결정된 타입을 무조건 설정
		if (request.getEtc() != null) {
			this.etc = request.getEtc();
		}
	}
}
