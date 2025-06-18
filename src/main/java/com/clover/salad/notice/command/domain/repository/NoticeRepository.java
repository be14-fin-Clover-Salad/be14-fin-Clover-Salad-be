package com.clover.salad.notice.command.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.notice.command.domain.aggregate.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
