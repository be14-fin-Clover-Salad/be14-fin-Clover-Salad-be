package com.clover.salad.goal.command.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.service.EmployeeQueryService;
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
	private final EmployeeQueryService employeeQueryService;
	
	/* 설명. 실적 목표 등록 */
	@Override
	public void registerGoal(List<GoalDTO> goalList, int employeeId) throws Exception {
		if(validateGoal(goalList, employeeId)) {
			for (GoalDTO goalDTO : goalList) {
				Goal goal = goalDTOToGoal(goalDTO);
				goalRepository.save(goal);
			}
		} else {
			throw new Exception("목표의 조건을 확인하고 설정해주세요!");
		}
	}
	
	/* 설명. 실적 목표가 회사에서 제시한 연간 목표 조건에 부합하는지 확인하는 메소드
	*   프론트에서 항목 별로 한 번 체크하고 최종 등록 전 체크
	* */
	private boolean validateGoal(List<GoalDTO> goalList, int employeeId) {
		
		/* 설명. 설정한 월간 목표들을 연간 목표로 변환 */
		GoalDTO yearlyGoal = new GoalDTO();
		Long[] goalArray = new Long[7];
		
		for (GoalDTO goalDTO : goalList) {
			goalDTO.setEmployeeId(employeeId);
			goalArray[0] += goalDTO.getRentalProductCount();
			goalArray[1] += goalDTO.getRentalRetentionCount();
			goalArray[2] += goalDTO.getTotalRentalAmount();
			goalArray[3] += goalDTO.getNewCustomerCount();
			goalArray[4] += goalDTO.getTotalRentalAmount();
			goalArray[5] += goalDTO.getCustomerFeedbackScore();
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
		
		/* TODO. 설명. 사원 id로 직급 뽑아오기 */
		String employeeLevel = "";
		// employeeQueryService
		
		/* 설명. 직급과 기간으로 회사의 연간 목표 조회 */
		DefaultGoalDTO defaultGoal = goalQueryService.searchDefaultGoalByLevelAndTargetYear(employeeLevel, yearlyGoal.getTargetDate());
		
		/* 설명. 회사의 연간 목표보다 설정한 목표가 높거나 같은지 체크 */
		if (yearlyGoal.getRentalProductCount() < defaultGoal.getRentalProductCount()) return false;
		if (yearlyGoal.getRentalRetentionCount() / yearlyGoal.getTotalRentalCount() < defaultGoal.getRentalRetentionRate()) return false;
		if (yearlyGoal.getNewCustomerCount() < defaultGoal.getNewCustomerCount()) return false;
		if (yearlyGoal.getTotalRentalAmount() < defaultGoal.getTotalRentalAmount()) return false;
		if (yearlyGoal.getCustomerFeedbackScore() / yearlyGoal.getCustomerFeedbackCount() < defaultGoal.getCustomerFeedbackScore()) return false;
		
		/* 설명. 개인 월간 목표가 월간 최저 목표(연간 목표 / 12 * 0.1 * 월간하한비율)를 넘는지 체크 */
		int monthlyRate = 7;
		int monthlyRentalProductCount = defaultGoal.getRentalProductCount() * monthlyRate / 120;
		int monthlyRentalRetentionRate = defaultGoal.getRentalRetentionRate() * monthlyRate / 10;
		int monthlyNewCustomerCount = defaultGoal.getNewCustomerCount() * monthlyRate / 120;
		long monthlyTotalRentalAmount = defaultGoal.getTotalRentalAmount() * monthlyRate / 120;
		int monthlyCustomerFeedbackScore = defaultGoal.getCustomerFeedbackScore() * monthlyRate / 10;
		
		for (GoalDTO goalDTO : goalList) {
			if (goalDTO.getRentalProductCount() < monthlyRentalProductCount) return false;
			if (goalDTO.getRentalRetentionCount() / goalDTO.getTotalRentalCount() < monthlyRentalRetentionRate) return false;
			if (goalDTO.getNewCustomerCount() < monthlyNewCustomerCount) return false;
			if (goalDTO.getTotalRentalAmount() < monthlyTotalRentalAmount) return false;
			if (goalDTO.getCustomerFeedbackScore() / goalDTO.getCustomerFeedbackCount() < monthlyCustomerFeedbackScore) return false;
		}
		
		return true;
	}
	
	private Goal goalDTOToGoal(GoalDTO goalDTO) {
		Goal goal = new Goal();
		goal.setRentalProductCount(goalDTO.getRentalProductCount());
		goal.setRentalRetentionCount(goalDTO.getRentalRetentionCount());
		goal.setTotalRentalAmount(goalDTO.getTotalRentalAmount());
		goal.setNewCustomerCount(goalDTO.getNewCustomerCount());
		goal.setTotalRentalAmount(goalDTO.getTotalRentalAmount());
		goal.setCustomerFeedbackScore(goalDTO.getCustomerFeedbackScore());
		goal.setCustomerFeedbackCount(goalDTO.getCustomerFeedbackCount());
		goal.setTargetDate(goalDTO.getTargetDate());
		goal.setEmployeeId(goalDTO.getEmployeeId());
		return goal;
	}
}
