package com.clover.salad.common.logging;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogAnalyzerController {

	private final LogAnalyzerService logAnalyzerService;

	@GetMapping("/analyze/{date}")
	public Map<String, Object> analyze(@PathVariable String date) {
		List<LogEntry> entries = logAnalyzerService.parseLogFile(date);
		return Map.of(
			"total", entries.size(),
			"entries", entries
		);
	}
}

