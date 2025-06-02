package com.clover.salad.customer.command.domain.aggregate.entity;

import java.time.LocalDate;

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

@Entity
@Table(name = "customer")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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

	@Column(length = 100)
	private String address;

	@Column(unique = true, length = 255)
	private String email;

	@Column(name = "register_at")
	private LocalDate registerAt;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	@Column(nullable = false, length = 20)
	private String type;

	@Column(length = 20)
	private String etc;

	public void update(Customer updated) {
		this.name = updated.name != null ? updated.name : this.name;
		this.birthdate = updated.birthdate != null ? updated.birthdate : this.birthdate;
		this.phone = updated.phone != null ? updated.phone : this.phone;
		this.address = updated.address != null ? updated.address : this.address;
		this.email = updated.email != null ? updated.email : this.email;
		this.type = updated.type != null ? updated.type : this.type;
		this.etc = updated.etc != null ? updated.etc : this.etc;
	}

	public void softDelete() {
		this.isDeleted = true;
	}
}
