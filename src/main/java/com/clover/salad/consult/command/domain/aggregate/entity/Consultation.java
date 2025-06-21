package com.clover.salad.consult.command.domain.aggregate.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consultation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consult_at", nullable = false)
    private LocalDateTime consultAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(length = 255)
    private String etc;

    @Column(name = "feedback_score", precision = 2, scale = 1)
    private BigDecimal feedbackScore;

    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @PrePersist
    protected void onCreate() {
        if (this.consultAt == null) {
            this.consultAt = LocalDateTime.now();
        }
    }

    @Builder
    public Consultation(String content, String etc, Integer employeeId, Integer customerId) {
        this.content = content;
        this.etc = etc;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.isDeleted = false;
    }

    public void update(Consultation updated) {
        if (updated.content != null && !updated.content.equals(this.content)) {
            this.content = updated.content;
        }
        if (updated.etc != null && !updated.etc.equals(this.etc)) {
            this.etc = updated.etc;
        }
    }
}
