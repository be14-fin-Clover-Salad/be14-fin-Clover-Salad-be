package com.clover.salad.notice.command.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.notice.command.application.dto.NoticeCreateRequest;
import com.clover.salad.notice.command.application.dto.NoticeUpdateRequest;
import com.clover.salad.notice.command.domain.aggregate.entity.EmployeeNotice;
import com.clover.salad.notice.command.domain.aggregate.entity.Notice;
import com.clover.salad.notice.command.domain.repository.EmployeeNoticeRepository;
import com.clover.salad.notice.command.domain.repository.NoticeRepository;

@Service
public class NoticeCommandServiceImpl implements NoticeCommandService {

	private final NoticeRepository noticeRepository;
	private final EmployeeNoticeRepository employeeNoticeRepository;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public NoticeCommandServiceImpl(NoticeRepository noticeRepository,
		EmployeeNoticeRepository employeeNoticeRepository, EmployeeRepository employeeRepository) {
		this.noticeRepository = noticeRepository;
		this.employeeNoticeRepository = employeeNoticeRepository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public void createNotice(NoticeCreateRequest request, int writerId) {
		Notice notice = new Notice();
		notice.setTitle(request.getTitle());
		notice.setContent(request.getContent());
		notice.setCreatedAt(LocalDateTime.now());
		notice.setEmployeeId(writerId);
		noticeRepository.save(notice);

		List<EmployeeNotice> employeeNotices = request.getTargetEmployeeId().stream()
			.map(empId -> {
				if (!employeeRepository.existsById(empId)) {
					throw new IllegalArgumentException("존재하지 않는 사원 ID입니다: " + empId);
				}
				EmployeeNotice employeeNotice = new EmployeeNotice();
				employeeNotice.setEmployeeId(empId);
				employeeNotice.setNoticeId(notice.getId());
				return employeeNotice;
			}).toList();

		employeeNoticeRepository.saveAll(employeeNotices);
	}

	@Override
	@Transactional
	public void updateNotice(int noticeId, NoticeUpdateRequest request, int writerId) {
		Notice notice = noticeRepository.findById(noticeId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지입니다."));

		if(notice.getEmployeeId() != writerId) {
			throw new SecurityException("작성자만 수정할 수 있습니다.");
		}

		notice.setTitle(request.getTitle());
		notice.setContent(request.getContent());
		noticeRepository.save(notice);

		List<EmployeeNotice> existingNotices = employeeNoticeRepository.findByNoticeId(noticeId);
		Map<Integer, EmployeeNotice> existingMap = existingNotices.stream()
			.collect(Collectors.toMap(EmployeeNotice::getEmployeeId, en -> en));

		List<Integer> newIds = request.getTargetEmployeeId();

		List<EmployeeNotice> toDelete = existingNotices.stream()
			.filter(en -> !newIds.contains(en.getEmployeeId()))
			.toList();

		List<EmployeeNotice> toAdd = newIds.stream()
			.filter(id -> !existingMap.containsKey(id))
			.map(id -> {
				if (!employeeRepository.existsById(id)) {
					throw new IllegalArgumentException("존재하지 않는 사원 ID입니다: " + id);
				}

				EmployeeNotice employeeNotice = new EmployeeNotice();
				employeeNotice.setEmployeeId(id);
				employeeNotice.setNoticeId(notice.getId());
				employeeNotice.setChecked(false);
				return employeeNotice;
			}).toList();

		employeeNoticeRepository.deleteAll(toDelete);
		employeeNoticeRepository.saveAll(toAdd);
	}
}
