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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "employee")
public class EmployeeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name="code", nullable = false)
	private String code;

	@Column(name="password", nullable = false)
	private String encPwd;

	@Column(name="name", nullable = false)
	private String name;

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "email", nullable = false)
	private String email;

	@Convert(converter = EmployeeLevelConverter.class)
	@Column(name = "level", nullable = false)
	private EmployeeLevel level;

	@Column(name="hire_date")
	private LocalDate hireDate;

	@Column(name="resign_date")
	private LocalDate resignDate;

	@Column(name="is_admin", nullable = false)
	private boolean isAdmin;

	@Column(name="is_deleted", nullable = false)
	private boolean isDeleted;

	@Column(name="work_place")
	private String workPlace;

	@Column(name="department_id", nullable = false)
	private int departmentId;

	@Column(name="profile", nullable = false)
	private int profile;

}
