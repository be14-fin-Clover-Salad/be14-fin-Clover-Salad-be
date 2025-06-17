package com.clover.salad.documentTemplate.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateDTO;

@Mapper
public interface DocumentTemplateQueryMapper {
	List<DocumentTemplateDTO> findAll();

	DocumentTemplateDTO findById(int id);
}
