package com.clover.salad.goal.command.application.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.InvalidSearchTermException;
import com.clover.salad.common.exception.NotFoundException;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.goal.command.application.dto.DefaultGoalDTO;
import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.domain.aggregate.entity.Goal;
import com.clover.salad.goal.command.domain.repository.GoalRepository;
import com.clover.salad.goal.query.service.GoalQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalCommandServiceImpl implements GoalCommandService {
	private final GoalRepository goalRepository;
	private final GoalQueryService goalQueryService;
	private final EmployeeRepository employeeRepository;
	
	/* 설명. 실적 목표 등록 */
	@Override
	public void registerGoal(List<GoalDTO> goalList, int employeeId) {
		EmployeeEntity employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
		
		if (validateGoal(goalList, employee)) {
			for (GoalDTO goalDTO : goalList) {
				Goal goal = goalDTOToGoal(goalDTO);
				goalRepository.save(goal);
			}
		} else {
			throw new InvalidSearchTermException();
		}
	}
	
	@Override
	public void changeGoal(List<GoalDTO> goalList, int employeeId) {
		EmployeeEntity employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
		
		if (validateGoal(goalList, employee)) {
			for (GoalDTO goalDTO : goalList) {
				Goal goal = goalRepository.findByEmployeeIdAndTargetDate(employee.getId(), goalDTO.getTargetDate());
				goalRepository.save(updateGoal(goal, goalDTO));
			}
		} else {
			throw new InvalidSearchTermException();
		}
	}
	
	@Override
	public void deleteGoal(List<GoalDTO> goalList) {
		for (GoalDTO goalDTO : goalList) {
			Goal goal = goalRepository.findByEmployeeIdAndTargetDate(goalDTO.getEmployeeId(), goalDTO.getTargetDate());
			goalRepository.delete(goal);
		}
	}
	
	/* 설명. 실적 목표가 회사에서 제시한 연간 목표 조건에 부합하는지 확인하는 메소드
	 *  프론트에서 항목 별로 한 번 체크하고 최종 등록 전 체크
	 * */
	private boolean validateGoal(List<GoalDTO> goalList, EmployeeEntity employee) {
		
		/* 설명. 설정한 월간 목표들을 연간 목표로 변환 */
		log.info("Changing GoalList To YearlyGoal");
		GoalDTO yearlyGoal = new GoalDTO();
		Long[] goalArray = new Long[7];
		Arrays.fill(goalArray, 0L);
		
		for (GoalDTO goalDTO : goalList) {
			goalDTO.setEmployeeId(employee.getId());
			goalArray[0] += goalDTO.getRentalProductCount();
			goalArray[1] += goalDTO.getRentalRetentionCount();
			if (goalDTO.getTotalRentalCount() == null || goalDTO.getTotalRentalCount() == 0) return false;
			goalArray[2] += goalDTO.getTotalRentalCount();
			goalArray[3] += goalDTO.getNewCustomerCount();
			goalArray[4] += goalDTO.getTotalRentalAmount();
			goalArray[5] += goalDTO.getCustomerFeedbackScore();
			if (goalDTO.getCustomerFeedbackCount() == null || goalDTO.getCustomerFeedbackCount() == 0) return false;
			goalArray[6] += goalDTO.getCustomerFeedbackCount();
		}
		
		yearlyGoal.setRentalProductCount(goalArray[0].intValue());
		yearlyGoal.setRentalRetentionCount(goalArray[1].intValue());
		yearlyGoal.setTotalRentalCount(goalArray[2].intValue());
		yearlyGoal.setNewCustomerCount(goalArray[3].intValue());
		yearlyGoal.setTotalRentalAmount(goalArray[4]);
		yearlyGoal.setCustomerFeedbackScore(goalArray[5].intValue());
		yearlyGoal.setCustomerFeedbackCount(goalArray[6].intValue());
		
		/* 설명. 월간 목표 하나에서 연도만 추출 */
		yearlyGoal.setTargetDate(goalList.get(0).getTargetDate() / 100);
		log.info("Yearly Goal Target Date: {}", yearlyGoal.getTargetDate());
		
		/* 설명. 사원 코드로 직급 뽑아오기 */
		log.info("Getting Employee Level");
		String employeeLevel = employee.getLevel().getLabel();
		
		/* 설명. 직급과 기간으로 회사의 연간 목표 조회 */
		log.info("Getting Default Goal");
		DefaultGoalDTO defaultGoal = goalQueryService.searchDefaultGoalByLevelAndTargetYear(employeeLevel, yearlyGoal.getTargetDate());
		log.info("Default Goal {}", defaultGoal);
		if (defaultGoal == null) throw new NotFoundException("'" + yearlyGoal.getTargetDate() + "'년에 '"+ employeeLevel +"' 직급의 목표가 없습니다.");
		
		/* 설명. 회사의 연간 목표보다 설정한 목표가 높거나 같은지 체크 */
		log.info("Checking Yearly Goal");
		if (yearlyGoal.getRentalProductCount() < defaultGoal.getRentalProductCount()) return false;
		log.info("1");
		if (yearlyGoal.getRentalRetentionCount() * 100 / yearlyGoal.getTotalRentalCount() < defaultGoal.getRentalRetentionRate()) return false;
		log.info("2");
		if (yearlyGoal.getNewCustomerCount() < defaultGoal.getNewCustomerCount()) return false;
		log.info("3");
		if (yearlyGoal.getTotalRentalAmount() < defaultGoal.getTotalRentalAmount()) return false;
		log.info("4");
		if (yearlyGoal.getCustomerFeedbackScore() / yearlyGoal.getCustomerFeedbackCount() < defaultGoal.getCustomerFeedbackScore()) return false;
		log.info("5");
		
		/* 설명. 개인 월간 목표가 월간 최저 목표(연간 목표 / 12 * 0.1 * 월간하한비율)를 넘는지 체크 */
		log.info("Checking Monthly Goal");
		int monthlyRate = 7;
		int monthlyRentalProductCount = defaultGoal.getRentalProductCount() * monthlyRate / 120;
		int monthlyRentalRetentionRate = defaultGoal.getRentalRetentionRate() * monthlyRate / 10;
		int monthlyNewCustomerCount = defaultGoal.getNewCustomerCount() * monthlyRate / 120;
		long monthlyTotalRentalAmount = defaultGoal.getTotalRentalAmount() * monthlyRate / 120;
		int monthlyCustomerFeedbackScore = defaultGoal.getCustomerFeedbackScore() * monthlyRate / 10;
		
		for (GoalDTO goalDTO : goalList) {
			if (goalDTO.getRentalProductCount() < monthlyRentalProductCount) return false;
			if (goalDTO.getRentalRetentionCount() * 100 / goalDTO.getTotalRentalCount() < monthlyRentalRetentionRate) return false;
			if (goalDTO.getNewCustomerCount() < monthlyNewCustomerCount) return false;
			if (goalDTO.getTotalRentalAmount() < monthlyTotalRentalAmount) return false;
			if (goalDTO.getCustomerFeedbackScore() / goalDTO.getCustomerFeedbackCount() < monthlyCustomerFeedbackScore) return false;
		}
		
		log.info("Goal Validated");
		return true;
	}
	private Goal goalDTOToGoal(GoalDTO goalDTO) {
		Goal goal = new Goal();
		updateGoal(goal, goalDTO);
		goal.setTargetDate(goalDTO.getTargetDate());
		goal.setEmployeeId(goalDTO.getEmployeeId());
		return goal;
	}
	
	private Goal updateGoal(Goal goal, GoalDTO goalDTO) {
		goal.setRentalProductCount(goalDTO.getRentalProductCount());
		goal.setRentalRetentionCount(goalDTO.getRentalRetentionCount());
		goal.setTotalRentalCount(goalDTO.getTotalRentalCount());
		goal.setNewCustomerCount(goalDTO.getNewCustomerCount());
		goal.setTotalRentalAmount(goalDTO.getTotalRentalAmount());
		goal.setCustomerFeedbackScore(goalDTO.getCustomerFeedbackScore().doubleValue() / 10);
		goal.setCustomerFeedbackCount(goalDTO.getCustomerFeedbackCount());
		return goal;
	}
}
