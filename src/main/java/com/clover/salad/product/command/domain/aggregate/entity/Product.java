package com.clover.salad.product.command.domain.aggregate.entity;

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
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 1;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "serial_number")
	private String serialNumber;
	
	@Column(name = "product_code")
	private String productCode;
	
	@Column(name = "company")
	private String company;
	
	@Column(name = "origin_cost")
	private int originCost;
	
	@Column(name = "rental_cost")
	private int rentalCost;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_deleted")
	private boolean isDeleted;
	
	@Column(name = "file_upload_id")
	private int fileUploadId;
}
