package com.clover.salad.performance.command.application.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.EmployeeNotFoundException;
import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.domain.aggregate.entity.EmployeePerformance;
import com.clover.salad.performance.command.domain.repository.EmployeePerformanceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceCommandServiceImpl implements PerformanceCommandService {
	
	private final EmployeePerformanceRepository employeePerformanceRepository;
	private final EmployeeQueryService employeeQueryService;
	private final ContractService contractService;
	
	@Override
	public void refreshContractPerformance(String employeeCode) {
		int employeeId = getEmployeeByCode(employeeCode).getId();
		
		/* 설명. 현재 시간을 targetDate 형식으로 */
		SimpleDateFormat yearMonth = new SimpleDateFormat("yyyyMM");
		Date now = new Date();
		int targetDate = Integer.parseInt(yearMonth.format(now));
		
		EmployeePerformance currentEP = employeePerformanceRepository.findByEmployeeIdAndTargetDate(employeeId, targetDate);
		
		ContractSearchDTO contractSearchDTO = new ContractSearchDTO();
		contractSearchDTO.setEmployeeId(employeeId);
		
		LocalDate nowLD = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate startDateStart = nowLD.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate startDateEnd = nowLD.with(TemporalAdjusters.lastDayOfMonth());
		contractSearchDTO.setStartDateStart(startDateStart);
		contractSearchDTO.setStartDateEnd(startDateEnd);
		
		List<ContractDTO> contractDTOList = contractService.searchContracts(contractSearchDTO);
		EmployeePerformanceDTO epDTO = new EmployeePerformanceDTO();
		for (ContractDTO contractDTO : contractDTOList) {
			contractDTO;
		}
		
		/* 설명. 조회 시 없으면 새로 만들기, 있으면 업데이트하기 */
		if (currentEP == null) {
			EmployeePerformance newEP = new EmployeePerformance();
			setEPDTOToEP(epDTO, newEP);
			employeePerformanceRepository.save(newEP);
		} else {
			setEPDTOToEP(epDTO,currentEP);
			employeePerformanceRepository.save(currentEP);
		}
	}
	
	private void setEPDTOToEP(EmployeePerformanceDTO dto, EmployeePerformance entity) {
		entity.setEmployeeId(dto.getEmployeeId());
		entity.setTargetDate(dto.getTargetDate());
		entity.setRentalProductCount(dto.getRentalProductCount());
		entity.setRentalRetentionCount(dto.getRentalRetentionCount());
		entity.setTotalRentalCount(dto.getTotalRentalCount());
		entity.setNewCustomerCount(dto.getNewCustomerCount());
		entity.setTotalRentalAmount(dto.getTotalRentalAmount());
		entity.setCustomerFeedbackScore(dto.getCustomerFeedbackScore().doubleValue() / 10);
		entity.setCustomerFeedbackCount(dto.getCustomerFeedbackCount());
	}
	
	private EmployeeQueryDTO getEmployeeByCode(String employeeCode) throws EmployeeNotFoundException {
		SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
		searchEmployeeDTO.setCode(employeeCode);
		List<EmployeeQueryDTO> employeeList = employeeQueryService.searchEmployees(searchEmployeeDTO);
		if (employeeList.isEmpty()) throw new EmployeeNotFoundException();
		return employeeList.get(0);
	}
}
