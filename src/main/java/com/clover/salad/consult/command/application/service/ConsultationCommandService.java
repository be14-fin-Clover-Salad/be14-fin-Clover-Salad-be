package com.clover.salad.consult.command.application.service;

import com.clover.salad.consult.command.application.dto.ConsultationCreateRequest;
import com.clover.salad.consult.command.application.dto.ConsultationUpdateRequest;

public interface ConsultationCommandService {
    void createConsultation(ConsultationCreateRequest request);

    void updateConsultation(int consultId, ConsultationUpdateRequest request);

    void deleteConsultation(int consultId);
}
