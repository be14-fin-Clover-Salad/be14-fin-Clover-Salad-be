package com.clover.salad.goal.command.domain.aggregate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_goal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Goal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "rental_product_count")
	private Integer rentalProductCount;
	
	@Column(name = "rental_retention_count")
	private Integer rentalRetentionCount;
	
	@Column(name = "total_rental_count")
	private Integer totalRentalCount;
	
	@Column(name = "new_customer_count")
	private Integer newCustomerCount;
	
	@Column(name = "total_rental_amount")
	private Long totalRentalAmount;
	
	@Column(name = "customer_feedback_score")
	private Double customerFeedbackScore;
	
	@Column(name = "customer_feedback_count")
	private Integer customerFeedbackCount;
	
	@Column(name = "target_date")
	private int targetDate;
	
	@Column(name = "employee_id")
	private int employeeId;
}
