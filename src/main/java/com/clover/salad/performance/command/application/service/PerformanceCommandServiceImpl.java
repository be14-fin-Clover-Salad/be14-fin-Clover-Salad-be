package com.clover.salad.performance.command.application.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.SourceGetter;
import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.EmployeeNotFoundException;
import com.clover.salad.contract.command.entity.ContractProductEntity;
import com.clover.salad.contract.command.repository.ContractProductRepository;
import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.customer.query.service.CustomerQueryService;
import com.clover.salad.employee.command.domain.repository.DepartmentRepository;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchRequestDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.performance.command.application.dto.DepartmentPerformanceDTO;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
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
	private final ContractProductRepository contractProductRepository;
	private final ModelMapper modelMapper;

	@Override
	public void refreshEmployeePerformance(String employeeCode, int targetDate) {
		int employeeId = getEmployeeByCode(employeeCode).getId();

		YearMonth yearMonth = YearMonth.of(targetDate / 100, targetDate % 100);
		LocalDate startDateStart = yearMonth.atDay(1);
		LocalDate startDateEnd = yearMonth.atEndOfMonth();

		EmployeePerformance currentEP = employeePerformanceRepository.findByEmployeeIdAndTargetDate(employeeId, targetDate);

		/* 계약 조회를 위한 검색 조건 생성 */
		ContractSearchDTO contractSearchDTO = ContractSearchDTO.builder()
			.employeeId(employeeId)
			.status("완료")
			.startDateStart(startDateStart)
			.startDateEnd(startDateEnd)
			.build();

		List<ContractDTO> contractDTOList = contractService.searchContracts(employeeId, contractSearchDTO);

		int rentalProductCount = 0;

		/* totalRentalCount 이번 달의 총 계약 수 */
		int totalRentalCount = contractDTOList.size();

		/* 현재 유지되는 계약을 확인하기 위해 이전 달 계약 수로 초기화 */
		int rentalRetentionCount = totalRentalCount;

		/* 총 계약 금액을 저장할 변수 */
		long totalRentalAmount = 0L;

		/* 새로운 고객을 중복없이 저장하기 위한 Set */
		Set<Integer> newCustomerIdSet = new HashSet<>();

		for (ContractDTO contractDTO : contractDTOList) {
			/* rentalProductCount 렌탈 상품 수 */
			List<ContractProductEntity> cpEntityList = contractProductRepository.findByContractId(contractDTO.getId());
			for (ContractProductEntity cpEntity : cpEntityList) {
				rentalProductCount += cpEntity.getQuantity();
			}
			/* rentalRetentionCount 계약 유지 건수 = 이번 달 총 계약 수 - 이번 달에 시작된 계약 수 */
			if (contractDTO.getStartDate().isAfter(startDateStart)
				|| contractDTO.getStartDate().isEqual(startDateStart)) {
				rentalRetentionCount--;
			}
			/* newCustomerCount 신규 고객 수: 리스트로 만들고 id 개수를 세는 로직 */
			// int currentCustomerId = contractDTO.getCustomerId();
			// LocalDate currentCustomerRegisterDate = LocalDate.parse(
			// 	customerQueryService.findCustomerById(currentCustomerId).getRegisterAt()
			// );
			// if (currentCustomerRegisterDate.isAfter(startDateStart)
			//  || currentCustomerRegisterDate.isEqual(startDateStart)) {
			// 	newCustomerIdSet.add(currentCustomerId);
			// }
			/* totalRentalAmount 총 렌탈 금액 */
			totalRentalAmount += contractDTO.getAmount();
		}

		EmployeePerformanceDTO epDTO = EmployeePerformanceDTO.builder()
			.employeeId(employeeId)
			.targetDate(targetDate)
			.rentalProductCount(rentalProductCount)
			.rentalRetentionCount(rentalRetentionCount)
			.totalRentalCount(totalRentalCount)
			.newCustomerCount(newCustomerIdSet.size())
			.totalRentalAmount(totalRentalAmount)
			.build();

		/* customerFeedbackScore 피드백 점수 총합 */


		/* customerFeedbackCount 피드백 한 사람 수 */


		/* 조회 시 없으면 새로 만들기, 있으면 업데이트하기 */
		mapIntToDividedDoubleAndIgnoreId(
			EmployeePerformanceDTO.class,
			EmployeePerformance.class,
			EmployeePerformanceDTO::getCustomerFeedbackScore,
			EmployeePerformance::setCustomerFeedbackScore,
			EmployeePerformance::setId
		);
		if (currentEP == null) {
			log.info("개인 실적 생성: {}", targetDate);
			EmployeePerformance newEP = modelMapper.map(epDTO, EmployeePerformance.class);
			employeePerformanceRepository.save(newEP);
		} else {
			log.info("개인 실적 갱신: {}", targetDate);
			modelMapper.map(epDTO, currentEP);
			employeePerformanceRepository.save(currentEP);
		}
	}

	@Override
	public void refreshDepartmentPerformance(String deptName, int targetDate) {
		int deptId = departmentRepository.findByName(deptName).getId();

		DepartmentPerformance currentDP = departmentPerformanceRepository.findByDepartmentIdAndTargetDate(deptId, targetDate);

		int dpRentalProductCount = 0;
		int dpRentalRetentionCount = 0;
		int dpTotalRentalCount = 0;
		int dpNewCustomerCount = 0;
		long dpTotalRentalAmount = 0L;
		int dpCustomerFeedbackScore = 0;
		int dpCustomerFeedbackCount = 0;

		List<EmployeePerformanceDTO> employeePerformanceDTOList =
			performanceQueryService.searchEmployeePerformanceByTargetDateAndDepartmentId(targetDate, deptId);
		for (EmployeePerformanceDTO epDTO : employeePerformanceDTOList) {
			dpRentalProductCount += epDTO.getRentalProductCount();
			dpRentalRetentionCount += epDTO.getRentalRetentionCount();
			dpTotalRentalCount += epDTO.getTotalRentalCount();
			dpNewCustomerCount += epDTO.getNewCustomerCount();
			dpTotalRentalAmount += epDTO.getTotalRentalAmount();
			dpCustomerFeedbackScore += epDTO.getCustomerFeedbackScore();
			dpCustomerFeedbackCount += epDTO.getCustomerFeedbackCount();
		}
		DepartmentPerformanceDTO dpDTO = DepartmentPerformanceDTO.builder()
			.departmentId(deptId)
			.targetDate(targetDate)
			.rentalProductCount(dpRentalProductCount)
			.rentalRetentionCount(dpRentalRetentionCount)
			.totalRentalCount(dpTotalRentalCount)
			.newCustomerCount(dpNewCustomerCount)
			.totalRentalAmount(dpTotalRentalAmount)
			.customerFeedbackScore(dpCustomerFeedbackScore)
			.customerFeedbackCount(dpCustomerFeedbackCount)
			.build();

		/* 조회 시 없으면 새로 만들기, 있으면 업데이트하기 */
		mapIntToDividedDoubleAndIgnoreId(
			DepartmentPerformanceDTO.class,
			DepartmentPerformance.class,
			DepartmentPerformanceDTO::getCustomerFeedbackScore,
			DepartmentPerformance::setCustomerFeedbackScore,
			DepartmentPerformance::setId
		);
		if (currentDP == null) {
			log.info("팀 실적 생성: {}", targetDate);
			DepartmentPerformance newDP = modelMapper.map(dpDTO, DepartmentPerformance.class);
			departmentPerformanceRepository.save(newDP);
		} else {
			log.info("팀 실적 갱신: {}", targetDate);
			modelMapper.map(dpDTO, currentDP);
			departmentPerformanceRepository.save(currentDP);
		}
	}

	public <S, D> void mapIntToDividedDoubleAndIgnoreId(
		Class<S> sourceClass,
		Class<D> destClass,
		SourceGetter<S> sourceGetter,
		DestinationSetter<D, Double> destSetter,
		DestinationSetter<D, Integer> idSetter
	) {
		/*
		 * dto에서는 10을 곱해 int 값으로 사용하던 customerFeedbackScore를
		 * entity를 통해 DB에 넣을 때 10으로 나누기
		 * */
		Converter<Integer, Double> converter = ctx -> {
			Integer source = ctx.getSource();
			return (source == null) ? null : source / 10.0;
		};

		modelMapper.typeMap(sourceClass, destClass)
			.addMappings(m -> m.using(converter).map(sourceGetter, destSetter))
			.addMappings(mapper -> mapper.skip(idSetter));
	}

	private EmployeeSearchResponseDTO getEmployeeByCode(String employeeCode) throws EmployeeNotFoundException {
		EmployeeSearchRequestDTO searchDTO = new EmployeeSearchRequestDTO();
		searchDTO.setCode(employeeCode);
		List<EmployeeSearchResponseDTO> employeeList = employeeQueryService.searchEmployees(searchDTO);
		if (employeeList.isEmpty()) throw new EmployeeNotFoundException();
		return employeeList.get(0);
	}
}
