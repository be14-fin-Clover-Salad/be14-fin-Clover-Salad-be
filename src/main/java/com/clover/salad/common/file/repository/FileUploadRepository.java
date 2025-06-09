package com.clover.salad.common.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.common.file.entity.FileUploadEntity;

public interface FileUploadRepository extends JpaRepository<FileUploadEntity, Integer> {
}
