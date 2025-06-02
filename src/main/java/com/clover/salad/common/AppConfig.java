package com.clover.salad.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.clover.salad.employee.command.application.dto.RequestRegisterDTO;
import com.clover.salad.employee.command.domain.aggregate.enums.EmployeeLevel;
import com.clover.salad.employee.command.domain.aggregate.vo.RequestRegisterEmployeeVO;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration()
			.setMatchingStrategy(MatchingStrategies.STRICT)
			.setFieldMatchingEnabled(true)
			.setFieldAccessLevel(AccessLevel.PRIVATE);

		modelMapper.typeMap(RequestRegisterEmployeeVO.class, RequestRegisterDTO.class)
			.addMappings(m -> m.using(ctx -> {
				String label = (String) ctx.getSource();
				return EmployeeLevel.fromLabel(label);
			}).map(RequestRegisterEmployeeVO::getLevel, RequestRegisterDTO::setLevel));

		return modelMapper;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}