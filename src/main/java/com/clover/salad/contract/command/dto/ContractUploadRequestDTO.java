package com.clover.salad.contract.command.dto;

import java.util.List;

import com.clover.salad.contract.document.entity.DocumentOrigin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractUploadRequestDTO {
	private CustomerDTO customer;
	private ContractDTO contract;
	private DocumentOrigin documentOrigin;
	private List<ProductDTO> products;

}