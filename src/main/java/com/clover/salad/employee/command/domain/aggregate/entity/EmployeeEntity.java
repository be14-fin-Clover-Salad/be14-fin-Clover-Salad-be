package com.clover.salad.employee.command.domain.aggregate.entity;

import java.time.LocalDate;
import com.clover.salad.employee.command.domain.aggregate.enums.EmployeeLevel;
import com.clover.salad.employee.command.domain.aggregate.enums.EmployeeLevelConverter;

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
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "employee")
public class EmployeeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "code")
	private String code;

	@Column(name = "password")
	private String password;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Convert(converter = EmployeeLevelConverter.class)
	@Column(name="level")
	private EmployeeLevel level;

	@Column(name = "hire_date")
	private LocalDate hireDate;

	@Column(name = "resign_date")
	private LocalDate resignDate;

	@Column(name = "is_admin")
	private boolean isAdmin;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	@Column(name = "work_place")
	private String workPlace;

	@Column(name = "department_id")
	private int departmentId;

	@Column(name="profile")
	private int profile;
}
