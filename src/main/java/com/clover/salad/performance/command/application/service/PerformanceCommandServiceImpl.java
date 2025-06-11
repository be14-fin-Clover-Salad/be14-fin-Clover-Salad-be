package com.clover.salad.performance.command.application.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.EmployeeNotFoundException;
import com.clover.salad.contract.command.entity.ContractProductEntity;
import com.clover.salad.contract.command.repository.ContractProductRepository;
import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.customer.query.service.CustomerQueryService;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.DepartmentRepository;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.performance.command.application.dto.DepartmentPerformanceDTO;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.application.dto.SearchTermDTO;
import com.clover.salad.performance.command.domain.aggregate.entity.DepartmentPerformance;
import com.clover.salad.performance.command.domain.aggregate.entity.EmployeePerformance;
import com.clover.salad.performance.command.domain.repository.DepartmentPerformanceRepository;
import com.clover.salad.performance.command.domain.repository.EmployeePerformanceRepository;
import com.clover.salad.performance.query.service.PerformanceQueryService;

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
	private final DepartmentRepository departmentRepository;
	private final DepartmentPerformanceRepository departmentPerformanceRepository;
	private final PerformanceQueryService performanceQueryService;
	private final EmployeeRepository employeeRepository;
	private final ContractProductRepository contractProductRepository;
	
	@Override
	public void refreshEmployeePerformance(String employeeCode, int targetDate) {
		int employeeId = getEmployeeByCode(employeeCode).getId();
		
		YearMonth yearMonth = YearMonth.of(targetDate / 100, targetDate % 100);
		LocalDate startDateStart = yearMonth.atDay(1);
		LocalDate startDateEnd = yearMonth.atEndOfMonth();
		
		EmployeePerformance currentEP = employeePerformanceRepository.findByEmployeeIdAndTargetDate(employeeId, targetDate);
		EmployeePerformanceDTO epDTO = new EmployeePerformanceDTO();
		
		/* 설명. 계약 조회를 위한 검색 조건 생성 */
		ContractSearchDTO contractSearchDTO = new ContractSearchDTO();
		contractSearchDTO.setEmployeeId(employeeId);
		/* 설명. 검색 조건 중 시간 설정 */
		contractSearchDTO.setStartDateStart(startDateStart);
		contractSearchDTO.setStartDateEnd(startDateEnd);
		
		List<ContractDTO> contractDTOList = contractService.searchContracts(employeeId, contractSearchDTO);
		
		int rentalProductCount = 0;
		int totalRentalCount = contractDTOList.size();
		/* 설명. totalRentalCount 이번 달의 총 계약 수 */
		epDTO.setTotalRentalCount(totalRentalCount);
		int rentalRetentionCount = totalRentalCount;
		long totalRentalAmount = 0L;
		Set<Integer> newCustomerIdSet = new HashSet<>();
		
		for (ContractDTO contractDTO : contractDTOList) {
			/* 설명. rentalProductCount 렌탈 상품 수 */
			List<ContractProductEntity> cpEntityList = contractProductRepository.findByContractId(contractDTO.getId());
			for (ContractProductEntity cpEntity : cpEntityList) {
				rentalProductCount += cpEntity.getQuantity();
			}
			/* 설명. rentalRetentionCount 계약 유지 건수 = 이번 달 총 계약 수 - 이번 달에 시작된 계약 수 */
			if (contractDTO.getStartDate().isAfter(startDateStart)
			 || contractDTO.getStartDate().isEqual(startDateStart)) {
				rentalRetentionCount--;
			}
			/* 설명. newCustomerCount 신규 고객 수: 리스트로 만들고 id 개수를 세는 로직 */
			int currentCustomerId = contractDTO.getCustomerId();
			LocalDate currentCustomerRegisterDate = LocalDate.parse(customerQueryService.findCustomerById(currentCustomerId).getRegisterAt());
			if (currentCustomerRegisterDate.isAfter(startDateStart)
			 || currentCustomerRegisterDate.isEqual(startDateStart)) {
				newCustomerIdSet.add(currentCustomerId);
			}
			/* 설명. totalRentalAmount 총 렌탈 금액 */
			totalRentalAmount += contractDTO.getAmount();
		}
		
		epDTO.setRentalProductCount(rentalProductCount);
		epDTO.setRentalRetentionCount(rentalRetentionCount);
		epDTO.setNewCustomerCount(newCustomerIdSet.size());
		epDTO.setTotalRentalAmount(totalRentalAmount);
		epDTO.setTargetDate(targetDate);
		epDTO.setEmployeeId(employeeId);
		
		/* 설명. customerFeedbackScore 피드백 점수 총합 */
		
		
		/* 설명. customerFeedbackCount 피드백 한 사람 수 */
		
		
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
	public void refreshDepartmentPerformance(String deptName, int targetDate) {
		int deptId = departmentRepository.findByName(deptName).getId();
		
		DepartmentPerformance currentDP = departmentPerformanceRepository.findByDepartmentIdAndTargetDate(deptId, targetDate);
		DepartmentPerformanceDTO dpDTO = new DepartmentPerformanceDTO();
		int dpRentalProductCount = 0;
		int dpRentalRetentionCount = 0;
		int dpTotalRentalCount = 0;
		int dpNewCustomerCount = 0;
		long dpTotalRentalAmount = 0L;
		int dpCustomerFeedbackScore = 0;
		int dpCustomerFeedbackCount = 0;
		
		SearchTermDTO searchTermDTO = new SearchTermDTO(targetDate, targetDate);
		List<EmployeeEntity> employeeList = employeeRepository.findByDepartmentIdAndIsAdmin(deptId, false);
		for (EmployeeEntity employee : employeeList) {
			String employeeCode = employee.getCode();
			List<EmployeePerformanceDTO> epDTOList = performanceQueryService.searchEmployeePerformanceByEmployeeCode(employeeCode, searchTermDTO);
			if (epDTOList != null && !epDTOList.isEmpty()) {
				EmployeePerformanceDTO epDTO = epDTOList.get(0);
				dpRentalProductCount += Optional.ofNullable(epDTO.getRentalProductCount()).orElse(0);
				dpRentalRetentionCount += Optional.ofNullable(epDTO.getRentalRetentionCount()).orElse(0);
				dpTotalRentalCount += Optional.ofNullable(epDTO.getTotalRentalCount()).orElse(0);
				dpNewCustomerCount += Optional.ofNullable(epDTO.getNewCustomerCount()).orElse(0);
				dpTotalRentalAmount += Optional.ofNullable(epDTO.getTotalRentalAmount()).orElse(0L);
				dpCustomerFeedbackScore += Optional.ofNullable(epDTO.getCustomerFeedbackScore()).orElse(0);
				dpCustomerFeedbackCount += Optional.ofNullable(epDTO.getCustomerFeedbackCount()).orElse(0);
			}
		}
		
		dpDTO.setDepartmentId(deptId);
		dpDTO.setTargetDate(targetDate);
		dpDTO.setRentalProductCount(dpRentalProductCount);
		dpDTO.setRentalRetentionCount(dpRentalRetentionCount);
		dpDTO.setTotalRentalCount(dpTotalRentalCount);
		dpDTO.setNewCustomerCount(dpNewCustomerCount);
		dpDTO.setTotalRentalAmount(dpTotalRentalAmount);
		dpDTO.setCustomerFeedbackScore(dpCustomerFeedbackScore);
		dpDTO.setCustomerFeedbackCount(dpCustomerFeedbackCount);
		
		/* 설명. 조회 시 없으면 새로 만들기, 있으면 업데이트하기 */
		if (currentDP == null) {
			DepartmentPerformance newDP = new DepartmentPerformance();
			setDPDTOToDP(dpDTO, newDP);
			departmentPerformanceRepository.save(newDP);
		} else {
			setDPDTOToDP(dpDTO,currentDP);
			departmentPerformanceRepository.save(currentDP);
		}
	}
	
	private void setDPDTOToDP(DepartmentPerformanceDTO dto, DepartmentPerformance entity) {
		entity.setDepartmentId(dto.getDepartmentId());
		entity.setTargetDate(dto.getTargetDate());
		entity.setRentalProductCount(Optional.ofNullable(dto.getRentalProductCount()).orElse(0));
		entity.setRentalRetentionCount(Optional.ofNullable(dto.getRentalRetentionCount()).orElse(0));
		entity.setTotalRentalCount(Optional.ofNullable(dto.getTotalRentalCount()).orElse(0));
		entity.setNewCustomerCount(Optional.ofNullable(dto.getNewCustomerCount()).orElse(0));
		entity.setTotalRentalAmount(Optional.ofNullable(dto.getTotalRentalAmount()).orElse(0L));
		entity.setCustomerFeedbackScore(Optional.ofNullable(dto.getCustomerFeedbackScore()).orElse(0).doubleValue() / 10);
		entity.setCustomerFeedbackCount(Optional.ofNullable(dto.getCustomerFeedbackCount()).orElse(0));
	}
	
	private void setEPDTOToEP(EmployeePerformanceDTO dto, EmployeePerformance entity) {
		entity.setEmployeeId(dto.getEmployeeId());
		entity.setTargetDate(dto.getTargetDate());
		entity.setRentalProductCount(Optional.ofNullable(dto.getRentalProductCount()).orElse(0));
		entity.setRentalRetentionCount(Optional.ofNullable(dto.getRentalRetentionCount()).orElse(0));
		entity.setTotalRentalCount(Optional.ofNullable(dto.getTotalRentalCount()).orElse(0));
		entity.setNewCustomerCount(Optional.ofNullable(dto.getNewCustomerCount()).orElse(0));
		entity.setTotalRentalAmount(Optional.ofNullable(dto.getTotalRentalAmount()).orElse(0L));
		entity.setCustomerFeedbackScore(Optional.ofNullable(dto.getCustomerFeedbackScore()).orElse(0).doubleValue() / 10);
		entity.setCustomerFeedbackCount(Optional.ofNullable(dto.getCustomerFeedbackCount()).orElse(0));
	}
	
	private EmployeeQueryDTO getEmployeeByCode(String employeeCode) throws EmployeeNotFoundException {
		SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
		searchEmployeeDTO.setCode(employeeCode);
		List<EmployeeQueryDTO> employeeList = employeeQueryService.searchEmployees(searchEmployeeDTO);
		if (employeeList.isEmpty()) throw new EmployeeNotFoundException();
		return employeeList.get(0);
	}
}
