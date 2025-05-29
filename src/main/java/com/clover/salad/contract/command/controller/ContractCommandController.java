package com.clover.salad.contract.command.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/command")
public class ContractCommandController {


	/*
	* 추후 개발 예정
	* */
	// @PostMapping("/contract/upload")
	// public ResponseEntity<?> uploadContract(@RequestParam MultipartFile file) {
	// 	return ResponseEntity.ok(ContractService.registContract(fileName));
	// }

}
