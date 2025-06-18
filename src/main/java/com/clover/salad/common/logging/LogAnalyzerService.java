package com.clover.salad.common.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class LogAnalyzerService {
	private static final Pattern LOG_PATTERN = Pattern.compile(
		"(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}).*Client Request - IP: ([^,]+), Method: ([^,]+), URI: ([^,]+), User-Agent: (.+)"
	);

	public List<LogEntry> parseLogFile(String date) {
		String file = "logs/client-requests." + date + ".log";
		Path path = Paths.get(file);
		if (!Files.exists(path)) return List.of();

		try {
			return Files.lines(path)
				.map(this::parseLine)
				.filter(Objects::nonNull)
				.toList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private LogEntry parseLine(String line) {
		Matcher matcher = LOG_PATTERN.matcher(line);
		if (matcher.find()) {
			return new LogEntry(
				LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)
			);
		}
		return null;
	}
}

