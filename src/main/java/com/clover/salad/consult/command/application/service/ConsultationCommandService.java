package com.clover.salad.consult.command.application.service;

import com.clover.salad.consult.command.application.dto.ConsultationCreateRequest;

public interface ConsultationCommandService {
    void createConsultation(ConsultationCreateRequest request);
}
