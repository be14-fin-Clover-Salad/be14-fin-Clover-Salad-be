package com.clover.salad.performance.command.application.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.EmployeeNotFoundException;
import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.customer.query.service.CustomerQueryService;
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
	private final CustomerQueryService customerQueryService;
	
	@Override
	public void refreshEmployeeContractPerformance(String employeeCode) {
		int employeeId = getEmployeeByCode(employeeCode).getId();
		
		/* 설명. 현재 시간을 targetDate 형식으로 */
		SimpleDateFormat yearMonth = new SimpleDateFormat("yyyyMM");
		Date now = new Date();
		int targetDate = Integer.parseInt(yearMonth.format(now));
		
		EmployeePerformance currentEP = employeePerformanceRepository.findByEmployeeIdAndTargetDate(employeeId, targetDate);
		EmployeePerformanceDTO epDTO = new EmployeePerformanceDTO();
		
		/* 설명. 계약 조회를 위한 검색 조건 생성 */
		ContractSearchDTO contractSearchDTO = new ContractSearchDTO();
		contractSearchDTO.setEmployeeId(employeeId);
		/* 설명. 검색 조건 중 시간 설정 */
		LocalDate nowLD = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate startDateStart = nowLD.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate startDateEnd = nowLD.with(TemporalAdjusters.lastDayOfMonth());
		contractSearchDTO.setStartDateStart(startDateStart);
		contractSearchDTO.setStartDateEnd(startDateEnd);
		
		List<ContractDTO> contractDTOList = contractService.searchContracts(contractSearchDTO);
		
		int rentalProductCount = 0;
		int totalRentalCount = contractDTOList.size();
		/* 설명. totalRentalCount 이번 달의 총 계약 수 */
		epDTO.setTotalRentalCount(totalRentalCount);
		int rentalRetentionCount = totalRentalCount;
		long totalRentalAmount = 0L;
		Set<Integer> newCustomerIdSet = new HashSet<>();
		
		for (ContractDTO contractDTO : contractDTOList) {
			/* 설명. rentalProductCount 렌탈 상품 수 */
			// List<ContractProductEntity> cpEntityList = contractProductRepository.findByContractId(contractDTO.getId());
			// for (ContractProductEntity cpEntity : cpEntityList) {
			// 	rentalProductCount += cpEntity.getQuantity();
			// }
			/* 설명. rentalRetentionCount 계약 유지 건수 = 이번 달 총 계약 수 - 이번 달에 시작된 계약 수 */
			if (contractDTO.getStartDate().isAfter(startDateStart)
			 || contractDTO.getStartDate().isEqual(startDateStart)) {
				rentalRetentionCount--;
			}
			/* 설명. newCustomerCount 신규 고객 수: 리스트로 만들고 id 개수를 세는 로직 */
			// int currentCustomerId = contractDTO.getCustomerId();
			// LocalDate currentCustomerRegisterDate = LocalDate.parse(customerQueryService.findCustomerById(currentCustomerId).getRegisterAt());
			// if (currentCustomerRegisterDate.isAfter(startDateStart)
			//  || currentCustomerRegisterDate.isEqual(startDateStart)) {
			// 	newCustomerIdSet.add(currentCustomerId);
			// }
			/* 설명. totalRentalAmount 총 렌탈 금액 */
			totalRentalAmount += contractDTO.getAmount();
		}
		
		epDTO.setRentalProductCount(rentalProductCount);
		epDTO.setRentalRetentionCount(rentalRetentionCount);
		epDTO.setNewCustomerCount(newCustomerIdSet.size());
		epDTO.setTotalRentalAmount(totalRentalAmount);
		
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
	
	@Override
	public void refreshEmployeeCustomerPerformance(String employeeCode) {
		int employeeId = getEmployeeByCode(employeeCode).getId();
		
		/* 설명. 현재 시간을 targetDate 형식으로 */
		SimpleDateFormat yearMonth = new SimpleDateFormat("yyyyMM");
		Date now = new Date();
		int targetDate = Integer.parseInt(yearMonth.format(now));
		
		EmployeePerformance currentEP = employeePerformanceRepository.findByEmployeeIdAndTargetDate(employeeId, targetDate);
		EmployeePerformanceDTO epDTO = new EmployeePerformanceDTO();
		
		/* 설명. customerFeedbackScore */
		
		/* 설명. customerFeedbackCount */
		
		
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
	
	@Override
	public void refreshDepartmentPerformance(String deptName) {
	
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
