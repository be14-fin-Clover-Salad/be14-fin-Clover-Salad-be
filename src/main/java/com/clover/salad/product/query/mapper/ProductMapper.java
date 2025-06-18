package com.clover.salad.product.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.product.command.application.dto.ProductDTO;
import com.clover.salad.product.command.application.dto.SearchTermDTO;

@Mapper
public interface ProductMapper {
	ProductDTO selectProductById(int productId);
	
	List<ProductDTO> selectProductList(SearchTermDTO searchTerm);
}
