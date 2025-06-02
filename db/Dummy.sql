-- 현실적인 전체 테이블 더미 데이터 (5~7개씩)
USE saladdb;
SET FOREIGN_KEY_CHECKS = 0;

-- DEPARTMENT
INSERT INTO DEPARTMENT (id, name, is_deleted, sup_dept_id)
VALUES (1, '본부', False, NULL),
       (2, '영업1팀', False, 1),
       (3, '영업2팀', False, 1),
       (4, '고객지원팀', False, 1),
       (5, '기술지원팀', False, 1);

-- FILE_UPLOAD
INSERT INTO FILE_UPLOAD (origin_file, rename_file, path, created_at, type)
VALUES ('profile1.png', 'uuid1.png', '/uploads/', '2025-05-28 02:13:22', '프로필'),
       ('contract1.png', 'uuid2.png', '/uploads/', '2025-05-28 02:13:22', '계약서'),
       ('product1.png', 'uuid3.png', '/uploads/', '2025-05-28 02:13:22', '상품')
     , ('profile2.png', 'uuid4.png', '/uploads/', '2025-05-29 23:34:38', '프로필')
     , ('contract2.png', 'uuid5.png', '/uploads/', '2025-05-29 23:34:38', '계약서')
     , ('product2.png', 'uuid6.png', '/uploads/', '2025-05-29 23:34:38', '상품')
     , ('profile3.png', 'uuid7.png', '/uploads/', '2025-05-29 23:34:38', '프로필')
     , ('contract3.png', 'uuid8.png', '/uploads/', '2025-05-29 23:34:38', '계약서')
     , ('product3.png', 'uuid9.png', '/uploads/', '2025-05-29 23:34:38', '상품')
     , ('profile4.png', 'uuid10.png', '/uploads/', '2025-05-29 23:34:38', '프로필');

-- EMPLOYEE
INSERT INTO EMPLOYEE (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted,
                      work_place, department_id, profile)
VALUES ('202501001', 'pass01', '홍길동', '01012345678', 'hong@example.com', '사원', '2025-01-01', NULL, FALSE, FALSE, '서울본사',
        1, 1),
       ('202501002', 'pass02', '홍길둉', '01012345679', 'hong1@example.com', '팀장', '2024-01-01', NULL, FALSE, FALSE,
        '강남씨티빌딩', 2, 2);

-- CUSTOMER
INSERT INTO CUSTOMER (id, name, birthdate, address, phone, email, register_at, is_deleted, type, etc)
VALUES (1, '홍길동', '1981-02-15', '도시1', '01088880001', 'customer1@email.com', '2025-05-18', False, '법인', NULL),
       (2, '이영희', '1982-03-15', '도시2', '01088880002', 'customer2@email.com', '2025-05-08', False, '개인', NULL),
       (3, '최준호', '1983-04-15', '도시3', '01088880003', 'customer3@email.com', '2025-04-28', False, '법인', 'VIP'),
       (4, '김예림', '1984-05-15', '도시4', '01088880004', 'customer4@email.com', '2025-04-18', False, '개인', NULL),
       (5, '박상우', '1985-06-15', '도시5', '01088880005', 'customer5@email.com', '2025-04-08', False, '법인', NULL),
       (6, '윤미래', '1986-07-15', '도시6', '01088880006', 'customer6@email.com', '2025-03-29', False, '개인', 'VIP'),
       (7, '정수빈', '1987-08-15', '도시7', '01088880007', 'customer7@email.com', '2025-03-19', False, '법인', NULL);

-- PRODUCT
INSERT INTO product (category, name, serial_number, product_code, company, origin_cost, rental_cost, description, is_deleted, file_upload_id)
VALUES ('냉장고', '오브제컬렉션', 'S834BB30', 'LG-REF-S834BB30', 'LG', 2300000, 43900, 'LG에서 만든 예쁜 냉장고', FALSE, 1)
     , ('TV', '2024 QLED 4K QDE1 (189 cm) + 3.1 ch 사운드바 B650D', 'KQ75QDE1-B6', 'SAMSUNG-TV-KQ75QDE1-B6', '삼성', 3369000, 67425, '삼성 QLED TV', FALSE, 1)
     , ('세탁기', '비스포크 그랑데 AI 세탁기', 'WF24B7600KW', 'SAMSUNG-WASH-WF24B7600KW', '삼성', 1890000, 37900, 'AI 기능을 탑재한 삼성 세탁기', FALSE, 2)
     , ('건조기', 'LG 트롬 듀얼 인버터 히트펌프', 'RH18VNA', 'LG-DRY-RH18VNA', 'LG', 1720000, 35500, '에너지 효율이 뛰어난 LG 건조기', FALSE, 2)
     , ('에어컨', '휘센 듀얼 에어컨 2in1', 'FQ18DADWE2', 'LG-AC-FQ18DADWE2', 'LG', 2490000, 46800, '여름 필수품, LG 휘센 에어컨', FALSE, 3)
     , ('청소기', '제트 무선 청소기 VS20A956B', 'VS20A956B', 'SAMSUNG-VAC-VS20A956B', '삼성', 1340000, 27900, '강력한 흡입력의 삼성 무선 청소기', FALSE, 3)
     , ('오븐', '비스포크 큐커 AI 오븐', 'MC32A7035KT', 'SAMSUNG-OVEN-MC32A7035KT', '삼성', 590000, 12500, '다기능 삼성 오븐', FALSE, 4)
     , ('식기세척기', '디오스 식기세척기 스팀', 'DUBJ1G', 'LG-DISH-DUBJ1G', 'LG', 1380000, 28900, '고온 스팀 살균 LG 식기세척기', FALSE, 4)
     , ('냉장고', '비스포크 냉장고 4도어', 'RF85T9111AP', 'SAMSUNG-REF-RF85T9111AP', '삼성', 3100000, 58900, '고급형 삼성 냉장고', FALSE, 5)
     , ('TV', 'LG OLED evo C3 65인치', 'OLED65C3KNA', 'LG-TV-OLED65C3KNA', 'LG', 2790000, 55300, '고화질 LG OLED TV', FALSE, 5)
     , ('세탁기', 'LG 트윈워시 세탁기', 'F21VDP', 'LG-WASH-F21VDP', 'LG', 1980000, 39200, '세탁과 탈수를 동시에 LG 트윈워시', FALSE, 6)
     , ('건조기', '삼성 AI 건조기', 'DV90T8240SH', 'SAMSUNG-DRY-DV90T8240SH', '삼성', 1790000, 36500, 'AI 제어 삼성 건조기', FALSE, 6)
     , ('에어컨', '무풍에어컨 클래식', 'AF17A6474TZ', 'SAMSUNG-AC-AF17A6474TZ', '삼성', 2650000, 49500, '무풍 냉방 삼성 에어컨', FALSE, 7)
     , ('청소기', '코드제로 A9S', 'A958VA', 'LG-VAC-A958VA', 'LG', 1190000, 24500, 'LG 코드제로 무선 청소기', FALSE, 7)
     , ('오븐', 'LG 광파오븐 MA324B', 'MA324B', 'LG-OVEN-MA324B', 'LG', 540000, 11900, '간편한 요리를 위한 LG 오븐', FALSE, 8)
     , ('식기세척기', '삼성 비스포크 식기세척기', 'DW60A8055UG', 'SAMSUNG-DISH-DW60A8055UG', '삼성', 1450000, 29900, '스마트 기능 탑재 식기세척기', FALSE, 8)
     , ('공기청정기', 'LG 퓨리케어 360° 공기청정기', 'AS351NNFA', 'LG-AP-AS351NNFA', 'LG', 1040000, 20900, '360도 청정 기능 LG 공기청정기', FALSE, 9)
     , ('공기청정기', '삼성 블루스카이 5000', 'AX60A5510WDD', 'SAMSUNG-AP-AX60A5510WDD', '삼성', 980000, 19900, '초미세먼지 제거 삼성 공기청정기', FALSE, 9)
     , ('전자레인지', 'LG 전자레인지 MW25S', 'MW25S', 'LG-MIW-MW25S', 'LG', 210000, 4900, '간단 요리를 위한 전자레인지', FALSE, 10)
     , ('제습기', '삼성 무풍 제습기', 'AY10R5171', 'SAMSUNG-DEH-AY10R5171', '삼성', 670000, 13900, '쾌적한 여름을 위한 삼성 제습기', FALSE, 10);

-- NOTICE
INSERT INTO NOTICE (id, title, content, created_at, is_deleted, employee_id)
VALUES (1, '중요 공지 1', '공지사항 내용 1입니다. 시스템 점검 안내 등', '2025-05-01 15:28:15.537438', False, 2),
       (2, '중요 공지 2', '공지사항 내용 2입니다. 시스템 점검 안내 등', '2025-05-04 17:46:16.537449', False, 3),
       (3, '중요 공지 3', '공지사항 내용 3입니다. 시스템 점검 안내 등', '2025-05-08 07:12:35.537462', False, 4),
       (4, '중요 공지 4', '공지사항 내용 4입니다. 시스템 점검 안내 등', '2025-05-11 00:26:06.537464', False, 5),
       (5, '중요 공지 5', '공지사항 내용 5입니다. 시스템 점검 안내 등', '2025-05-13 13:39:39.537466', False, 6),
       (6, '중요 공지 6', '공지사항 내용 6입니다. 시스템 점검 안내 등', '2025-05-17 06:21:27.537468', False, 7),
       (7, '중요 공지 7', '공지사항 내용 7입니다. 시스템 점검 안내 등', '2025-05-20 07:56:26.537470', False, 1);

-- QNA
INSERT INTO QNA (id, title, content, created_at, answer_status, answer_content, is_deleted, employee_id)
VALUES (1, '문의 제목 1', '이 제품은 어떤 기능이 있나요?', '2025-05-02 06:54:50.537606', '답변완료', '답변 드리겠습니다.', False, 2),
       (2, '문의 제목 2', '이 제품은 어떤 기능이 있나요?', '2025-05-04 12:21:07.537610', '미답변', NULL, False, 3),
       (3, '문의 제목 3', '이 제품은 어떤 기능이 있나요?', '2025-05-08 01:02:51.537612', '답변완료', '답변 드리겠습니다.', False, 4),
       (4, '문의 제목 4', '이 제품은 어떤 기능이 있나요?', '2025-05-10 22:06:49.537613', '미답변', NULL, False, 5),
       (5, '문의 제목 5', '이 제품은 어떤 기능이 있나요?', '2025-05-13 10:19:10.537615', '답변완료', '답변 드리겠습니다.', False, 6),
       (6, '문의 제목 6', '이 제품은 어떤 기능이 있나요?', '2025-05-16 23:24:34.537616', '미답변', NULL, False, 7),
       (7, '문의 제목 7', '이 제품은 어떤 기능이 있나요?', '2025-05-20 06:11:17.537618', '답변완료', '답변 드리겠습니다.', False, 1);

-- CONTRACT
INSERT INTO CONTRACT (id, code, created_at, start_date, end_date, status, amount, bank_name, bank_account, payment_day,
                      deposit_owner, relationship, payment_email, is_deleted, etc, document_origin_id, customer_id,
                      employee_id)
VALUES (1, 'CNTR2024001', '2025-05-01 13:16:26.385656', '2025-05-26', '2026-05-28', '완료', 1100000, '신한은행',
        '110-001-56789', 11, '예금주1', '본인', 'user1@email.com', False, NULL, 1, 2, 2),
       (2, 'CNTR2024002', '2025-05-05 07:11:14.385684', '2025-05-24', '2026-05-28', '진행중', 1200000, '신한은행',
        '110-002-56789', 12, '예금주2', '본인', 'user2@email.com', False, NULL, 1, 3, 3),
       (3, 'CNTR2024003', '2025-05-08 03:27:11.385696', '2025-05-22', '2026-05-28', '완료', 1300000, '신한은행',
        '110-003-56789', 13, '예금주3', '본인', 'user3@email.com', False, NULL, 1, 4, 4),
       (4, 'CNTR2024004', '2025-05-10 14:09:14.385705', '2025-05-20', '2026-05-28', '진행중', 1400000, '신한은행',
        '110-004-56789', 14, '예금주4', '본인', 'user4@email.com', False, NULL, 1, 5, 5),
       (5, 'CNTR2024005', '2025-05-14 00:26:21.385715', '2025-05-18', '2026-05-28', '완료', 1500000, '신한은행',
        '110-005-56789', 15, '예금주5', '본인', 'user5@email.com', False, NULL, 1, 6, 6),
       (6, 'CNTR2024006', '2025-05-16 02:13:58.385726', '2025-05-16', '2026-05-28', '진행중', 1600000, '신한은행',
        '110-006-56789', 16, '예금주6', '본인', 'user6@email.com', False, NULL, 1, 7, 7),
       (7, 'CNTR2024007', '2025-05-14 02:13:58.385736', '2025-05-14', '2026-05-28', '완료', 1700000, '신한은행',
        '110-007-56789', 17, '예금주7', '본인', 'user7@email.com', False, NULL, 1, 1, 1);

-- CONSULTATION
INSERT INTO CONSULTATION (id, consult_at, content, is_deleted, etc, feedback_score, employee_id, customer_id)
VALUES (1, '2025-05-01 13:18:08.385986', '1번 고객의 제품 관련 상담 내용입니다.', False, NULL, 5.0, 2, 1),
       (2, '2025-05-04 17:13:13.385993', '2번 고객의 제품 관련 상담 내용입니다.', False, NULL, 4.0, 3, 2),
       (3, '2025-05-08 00:51:44.385996', '3번 고객의 제품 관련 상담 내용입니다.', False, NULL, 5.0, 4, 3),
       (4, '2025-05-11 07:48:14.385999', '4번 고객의 제품 관련 상담 내용입니다.', False, NULL, 4.0, 5, 4),
       (5, '2025-05-14 01:26:36.386004', '5번 고객의 제품 관련 상담 내용입니다.', False, NULL, 5.0, 6, 5),
       (6, '2025-04-28 02:13:58.386007', '6번 고객의 제품 관련 상담 내용입니다.', False, NULL, 4.0, 7, 6),
       (7, '2025-04-23 02:13:58.386011', '7번 고객의 제품 관련 상담 내용입니다.', False, NULL, 5.0, 1, 7);

-- DOCUMENT_TEMPLATE
INSERT INTO DOCUMENT_TEMPLATE (id, name, version, description, created_at, is_deleted, file_upload_id)
VALUES (1, '계약서 템플릿1', 'v1.1', '설명 1', '2025-05-01 23:28:44.466860', False, 1),
       (2, '계약서 템플릿2', 'v1.2', '설명 2', '2025-05-04 17:41:40.466865', False, 2),
       (3, '계약서 템플릿3', 'v1.3', '설명 3', '2025-05-08 01:46:46.466867', False, 3),
       (4, '계약서 템플릿4', 'v1.4', '설명 4', '2025-05-11 05:09:30.466868', False, 4),
       (5, '계약서 템플릿5', 'v1.5', '설명 5', '2025-05-14 06:37:43.466870', False, 5),
       (6, '계약서 템플릿6', 'v1.6', '설명 6', '2025-05-28 02:14:09.466872', False, 6),
       (7, '계약서 템플릿7', 'v1.7', '설명 7', '2025-05-28 02:14:09.466873', False, 7);

-- DOCUMENT_ORIGIN
INSERT INTO DOCUMENT_ORIGIN (id, created_at, is_deleted, document_template_id, file_upload_id)
VALUES (1, '2025-05-02 05:29:54.466978', False, 1, 1),
       (2, '2025-05-05 06:35:25.466979', False, 2, 2),
       (3, '2025-05-07 23:21:51.466979', False, 3, 3),
       (4, '2025-05-11 04:05:03.466980', False, 4, 4),
       (5, '2025-05-13 18:06:55.466980', False, 5, 5),
       (6, '2025-05-28 02:14:09.466981', False, 6, 6),
       (7, '2025-05-28 02:14:09.466981', False, 7, 7);

-- APPROVAL
INSERT INTO APPROVAL (id, code, title, content, req_date, aprv_date, state, comment, req_id, aprv_id, contract_id)
VALUES (1, 'APR2024001', '승인 요청 제목1', '승인 요청 내용1', '2025-05-01 23:48:33.467055', '2025-05-01 23:48:33.467056', '반려',
        '코멘트1', 2, 3, 1),
       (2, 'APR2024002', '승인 요청 제목2', '승인 요청 내용2', '2025-05-04 18:38:04.467058', '2025-05-04 18:38:04.467059', '승인',
        '코멘트2', 3, 4, 2),
       (3, 'APR2024003', '승인 요청 제목3', '승인 요청 내용3', '2025-05-07 20:34:51.467061', '2025-05-07 20:34:51.467061', '반려',
        '코멘트3', 4, 5, 3),
       (4, 'APR2024004', '승인 요청 제목4', '승인 요청 내용4', '2025-05-11 00:29:46.467062', '2025-05-11 00:29:46.467063', '승인',
        '코멘트4', 5, 6, 4),
       (5, 'APR2024005', '승인 요청 제목5', '승인 요청 내용5', '2025-05-13 21:16:18.467064', '2025-05-13 21:16:18.467065', '반려',
        '코멘트5', 6, 7, 5),
       (6, 'APR2024006', '승인 요청 제목6', '승인 요청 내용6', '2025-05-28 02:14:09.467066', '2025-05-28 02:14:09.467067', '승인',
        '코멘트6', 7, 1, 6),
       (7, 'APR2024007', '승인 요청 제목7', '승인 요청 내용7', '2025-05-28 02:14:09.467068', '2025-05-28 02:14:09.467068', '반려',
        '코멘트7', 1, 2, 7);

-- SALES
INSERT INTO SALES (id, sales_date, department, employee_name, amount, is_deleted, contract_id)
VALUES (1, '2025-05-28', '영업2팀', '직원1', 1050000, False, 1),
       (2, '2025-05-28', '영업3팀', '직원2', 1100000, False, 2),
       (3, '2025-05-28', '영업1팀', '직원3', 1150000, False, 3),
       (4, '2025-05-28', '영업2팀', '직원4', 1200000, False, 4),
       (5, '2025-05-28', '영업3팀', '직원5', 1250000, False, 5),
       (6, '2025-05-28', '영업1팀', '직원6', 1300000, False, 6),
       (7, '2025-05-28', '영업2팀', '직원7', 1350000, False, 7);

-- CONTRACT_PRODUCT
INSERT INTO CONTRACT_PRODUCT (id, quantity, contract_id, product_id)
VALUES (1, 4, 1, 1),
       (2, 3, 2, 2),
       (3, 5, 3, 3),
       (4, 4, 4, 4),
       (5, 2, 5, 5),
       (6, 1, 6, 6),
       (7, 5, 7, 7);

-- EMPLOYEE_GOAL
INSERT INTO EMPLOYEE_GOAL (rental_product_count, rental_retention_count, total_rental_count, new_customer_count,
                           total_rental_amount, customer_feedback_score, customer_feedback_count, target_date,
                           employee_id)
VALUES (7, 82, 101, 5, 1200000, 4.0, 51, 202405, 2),
       (8, 83, 102, 6, 1300000, 4.5, 52, 202405, 3),
       (9, 84, 103, 7, 1400000, 4.0, 53, 202405, 4),
       (10, 85, 104, 8, 1500000, 4.5, 54, 202405, 5),
       (11, 86, 105, 9, 1600000, 4.0, 55, 202404, 2),
       (11, 86, 105, 9, 1600000, 4.0, 55, 202404, 3),
       (11, 86, 105, 9, 1600000, 4.0, 55, 202404, 4),
       (11, 86, 105, 9, 1600000, 4.0, 55, 202404, 5),
       (11, 86, 105, 9, 1600000, 4.0, 55, 202404, 6),
       (9, 84, 103, 7, 1400000, 4.0, 53, 202401, 4),
       (9, 84, 103, 7, 1400000, 4.0, 53, 202402, 4),
       (9, 84, 103, 7, 1400000, 4.0, 53, 202403, 4),
       (12, 87, 106, 10, 1700000, 4.5, 56, 202405, 7);

-- DEFAULT_GOAL
INSERT INTO DEFAULT_GOAL (id, level, rental_product_count, rental_retention_rate, new_customer_count,
                          total_rental_amount, customer_feedback_score, target_year)
VALUES (1, '사원', 6, 85, 4, 1250000, 4.3, 2024),
       (2, '대리', 7, 85, 5, 1300000, 4.0, 2024),
       (3, '과장', 8, 85, 6, 1350000, 4.3, 2024),
       (4, '부장', 9, 85, 7, 1400000, 4.0, 2024),
       (5, '차장', 10, 85, 8, 1450000, 4.3, 2024),
       (6, '팀장', 11, 85, 9, 1500000, 4.0, 2024),
       (7, '본부장', 12, 85, 10, 1550000, 4.3, 2024);

-- EMPLOYEE_NOTICE
INSERT INTO EMPLOYEE_NOTICE (id, is_checked, employee_id, notice_id)
VALUES (1, False, 1, 1),
       (2, False, 2, 2),
       (3, False, 3, 3),
       (4, False, 4, 4),
       (5, False, 5, 5),
       (6, False, 6, 6),
       (7, False, 7, 7);

-- DEPARTMENT_PERFORMANCE
INSERT INTO DEPARTMENT_PERFORMANCE (id, rental_product_count, rental_retention_count, total_rental_count,
                                    new_customer_count,
                                    total_rental_amount, customer_feedback_score, customer_feedback_count, target_date,
                                    department_id)
VALUES (1, 6, 81, 100, 4, 1100000, 4.5, 50, 202405, 1),
       (2, 7, 82, 101, 5, 1200000, 4.0, 51, 202405, 2),
       (3, 8, 83, 102, 6, 1300000, 4.5, 52, 202405, 3),
       (4, 9, 84, 103, 7, 1400000, 4.0, 53, 202405, 4),
       (5, 10, 85, 104, 8, 1500000, 4.5, 54, 202405, 5),
       (6, 11, 86, 105, 9, 1600000, 4.0, 55, 202405, 6),
       (7, 12, 87, 106, 10, 1700000, 4.5, 56, 202405, 7);

-- EMPLOYEE_PERFORMANCE
INSERT INTO EMPLOYEE_PERFORMANCE (id, rental_product_count, rental_retention_count, total_rental_count,
                                  new_customer_count,
                                  total_rental_amount, customer_feedback_score, customer_feedback_count, target_date,
                                  employee_id)
VALUES (1, 6, 81, 100, 4, 1100000, 4.5, 50, 202405, 1),
       (2, 7, 82, 101, 5, 1200000, 4.0, 51, 202405, 2),
       (3, 8, 83, 102, 6, 1300000, 4.5, 52, 202405, 3),
       (4, 9, 84, 103, 7, 1400000, 4.0, 53, 202405, 4),
       (5, 10, 85, 104, 8, 1500000, 4.5, 54, 202405, 5),
       (6, 11, 86, 105, 9, 1600000, 4.0, 55, 202405, 6),
       (7, 12, 87, 106, 10, 1700000, 4.5, 56, 202405, 7);
