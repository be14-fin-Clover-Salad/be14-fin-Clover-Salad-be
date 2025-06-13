package com.clover.salad.notice.command.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.notice.command.domain.aggregate.entity.EmployeeNotice;

@Repository
public interface EmployeeNoticeRepository extends JpaRepository<EmployeeNotice, Long> {
	List<EmployeeNotice> findByNoticeId(int id);

	Optional<EmployeeNotice> findByNoticeIdAndEmployeeId(int noticeId, int employeeId);
}
