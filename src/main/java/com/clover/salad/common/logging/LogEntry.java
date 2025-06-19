package com.clover.salad.common.logging;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {
	private LocalDateTime timestamp;
	private String clientIp;
	private String method;
	private String uri;
	private String userAgent;
}
