package com.clover.salad.contract.command.service.parser;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PdfParsingStrategyRouter {

	// Spring이 주입하는 모든 PdfContractParsingStrategy 구현체들
	private final List<PdfContractParsingStrategy> strategyList;

	// templateId 기준으로 전략을 등록
	private final Map<Integer, PdfContractParsingStrategy> strategies = new HashMap<>();

	@PostConstruct
	public void init() {
		for (PdfContractParsingStrategy strategy : strategyList) {
			// 각 전략 클래스마다 조건에 따라 등록
			if (strategy instanceof DefaultTemplateParser) {
				strategies.put(1, strategy);  // Template ID 1 → DefaultTemplateParser
			}

			// 추후 템플릿 확장 시 여기에 추가
			// if (strategy instanceof AnotherTemplateParser) {
			//     strategies.put(2, strategy);
			// }
		}
	}

	public PdfContractParsingStrategy getStrategy(int templateId) {
		PdfContractParsingStrategy strategy = strategies.get(templateId);
		if (strategy == null) {
			throw new IllegalArgumentException("템플릿 ID " + templateId + "에 대한 파싱 전략이 존재하지 않습니다.");
		}
		return strategy;
	}
}
