package com.clover.salad.employee.query.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProfileQueryDTO {
	private int id;
	private String originFile;
	private String renameFile;
	private String path;
	private LocalDateTime createdAt;
	private String type;
}