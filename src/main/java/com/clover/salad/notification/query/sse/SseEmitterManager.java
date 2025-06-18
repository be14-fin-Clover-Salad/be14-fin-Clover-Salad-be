package com.clover.salad.notification.query.sse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SseEmitterManager {
	private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

	public SseEmitter connect(int employeeId) {
		log.info("[SSE] Emitter 등록 시작 - employeeId: {}", employeeId);

		SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
		emitters.put(employeeId, emitter);

		emitter.onCompletion(() -> {
			emitters.remove(employeeId);
			log.info("[SSE] 연결 종료 - employeeId: {}", employeeId);
		});
		emitter.onTimeout(() -> {
			emitters.remove(employeeId);
			log.warn("[SSE] 타임아웃 발생 - employeeId: {}", employeeId);
		});
		emitter.onError(e -> {
			emitters.remove(employeeId);
			log.error("[SSE] 에러 발생 - employeeId: {}, 오류: {}", employeeId, e.getMessage());
		});

		return emitter;
	}

	public void send(int employeeId, Object data) {
		SseEmitter emitter = emitters.get(employeeId);
		log.info("[SSE] 알림 전송 시도 - employeeId: {}, emitter 존재 여부: {}", employeeId, emitter != null);

		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().name("notification").data(data));
				log.info("[SSE] 알림 전송 성공 - employeeId: {}", employeeId);
			} catch (IOException e) {
				emitters.remove(employeeId);
				log.error("[SSE] 알림 전송 실패 - employeeId: {}, 이유: {}", employeeId, e.getMessage());
			}
		} else {
			log.warn("[SSE] 전송 실패 - employeeId={}의 emitter가 존재하지 않음", employeeId);
		}
	}
}

