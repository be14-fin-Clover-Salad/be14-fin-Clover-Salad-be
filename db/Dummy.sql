-- 현실적인 전체 테이블 더미 데이터 (5~7개씩)
USE saladdb;
SET FOREIGN_KEY_CHECKS = 0;

-- DEPARTMENT
-- 미배정 부서 (id=1)
INSERT INTO department (name, is_deleted, sup_dept_id) VALUES
    ('미배정', FALSE, NULL);

-- 상위 부서 (id=2~10)\
INSERT INTO department (name, is_deleted, sup_dept_id) VALUES
('인사부', FALSE, NULL),
('마케팅부', FALSE, NULL),
('재무/회계부', FALSE, NULL),
('생산부', FALSE, NULL),
('연구개발부', FALSE, NULL),
('IT/정보시스템부', FALSE, NULL),
('전략기획부', FALSE, NULL),
('영업부', FALSE, NULL),
('법무부', FALSE, NULL);

-- 하위 부서 (sup_dept_id = 상위 부서 id 반영)
INSERT INTO department (name, is_deleted, sup_dept_id) VALUES
-- 인사부(2)
('인재채용팀', FALSE, 2),
('인사운영팀', FALSE, 2),
('노무관리팀', FALSE, 2),

-- 마케팅부(3)
('브랜드팀', FALSE, 3),
('콘텐츠팀', FALSE, 3),
('광고팀', FALSE, 3),
('프로모션팀', FALSE, 3),

-- 재무/회계부(4)
('재무팀', FALSE, 4),
('회계팀', FALSE, 4),
('자금팀', FALSE, 4),

-- 생산부(5)
('생산1팀', FALSE, 5),
('생산2팀', FALSE, 5),
('생산기술팀', FALSE, 5),
('품질관리팀', FALSE, 5),

-- 연구개발부(6)
('연구1팀', FALSE, 6),
('연구2팀', FALSE, 6),
('제품개발팀', FALSE, 6),

-- IT/정보시스템부(7)
('IT운영팀', FALSE, 7),
('인프라팀', FALSE, 7),
('개발팀', FALSE, 7),
('보안팀', FALSE, 7),

-- 전략기획부(8)
('전략팀', FALSE, 8),
('기획팀', FALSE, 8),
('사업개발팀', FALSE, 8),

-- 영업부(9)
('영업1팀', FALSE, 9),
('영업2팀', FALSE, 9),
('영업3팀', FALSE, 9),
('영업4팀', FALSE, 9),
('영업5팀', FALSE, 9),

-- 법무부(10)
('법무지원팀', FALSE, 10),
('계약검토팀', FALSE, 10);

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
-- 관리자 더미
INSERT INTO employee (id, code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
(-1, 'ADMIN', '$2a$10$eu/cmPGCdI.cJKQHSi12..fq50TgXsJ3UjKg/nB4SC6rvAyK0woWW', '관리자', '01000000000', 'admin@saladerp.com', '관리자', '1000-01-01', NULL, TRUE, FALSE, '관리자', 1, 1);

-- 사원, 팀장 더미
-- 영업 1팀
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202501001', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq','강수지','01038679572','teamflover_s@naver.com','사원','2025-01-01',NULL,FALSE,FALSE,'서울강남',35,1),
('202501002', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq','고성연','01019990502','teamflover_j@naver.com','주임','2025-01-01',NULL,FALSE,FALSE,'서울강남',35,1),
('202501003', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq','고윤석','01095009870','teamflover_d@naver.com','대리','2025-01-01',NULL,FALSE,FALSE,'서울강남',35,1),
('202501004', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq','김성민','01046792202','teamflover_k@naver.com','과장','2025-01-01',NULL,FALSE,FALSE,'서울강남',35,1),
('202501005', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq','이청민','01020000101','teamflover_t@naver.com','팀장','2025-01-01',NULL,FALSE,FALSE,'서울강남',35,1);

-- 영업 2팀
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503993', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최은경', '01000000001', '202503993@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울강북', 36, 1),
('201811926', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이주은', '01000000002', '201811926@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울강북', 36, 1),
('201207641', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤수수', '01000000003', '201207641@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울강북', 36, 1),
('200604639', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최민진', '01000000004', '200604639@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울강북', 36, 1),
('200001446', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤민하', '01000000005', '200001446@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울강북', 36, 1);

-- 영업 3팀
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503665', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김슬민', '01000000006', '202503665@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '인천부평', 37, 1),
('201811852', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이은민', '01000000007', '201811852@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '인천부평', 37, 1),
('201207899', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정석하', '01000000008', '201207899@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '인천부평', 37, 1),
('200604962', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한석슬', '01000000009', '200604962@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '인천부평', 37, 1),
('200001225', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임석경', '01000000010', '200001225@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '인천부평', 37, 1);

-- 영업 4팀
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503444', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정하슬', '01000000011', '202503444@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '경기성남', 38, 1),
('201811566', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이진하', '01000000012', '201811566@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '경기성남', 38, 1),
('201207130', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '장현우', '01000000013', '201207130@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '경기성남', 38, 1),
('200604252', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최주호', '01000000014', '200604252@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '경기성남', 38, 1),
('200001223', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박현주', '01000000015', '200001223@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '경기성남', 38, 1);

-- 영업 5팀
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503358', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김은수', '01000000016', '202503358@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '부산해운대', 39, 1),
('201811460', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한호은', '01000000017', '201811460@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '부산해운대', 39, 1),
('201207841', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한진경', '01000000018', '201207841@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '부산해운대', 39, 1),
('200604792', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한슬수', '01000000019', '200604792@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '부산해운대', 39, 1),
('200001749', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임하진', '01000000020', '200001749@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '부산해운대', 39, 1);

-- 인사부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503563', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정현호', '01000000021', '202503563@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 11, 1),
('201811274', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '장경석', '01000000022', '201811274@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 11, 1),
('201207741', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박석진', '01000000023', '201207741@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 11, 1),
('200604370', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤연연', '01000000024', '200604370@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 11, 1),
('200001565', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조경석', '01000000025', '200001565@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 11, 1),
('202503352', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이연은', '01000000026', '202503352@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 12, 1),
('201811428', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조석연', '01000000027', '201811428@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 12, 1),
('201207208', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '장은슬', '01000000028', '201207208@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 12, 1),
('200604186', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최주석', '01000000029', '200604186@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 12, 1),
('200001928', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최민호', '01000000030', '200001928@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 12, 1),
('202503919', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김수호', '01000000031', '202503919@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 13, 1),
('201811783', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최진호', '01000000032', '201811783@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 13, 1),
('201207227', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임석수', '01000000033', '201207227@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 13, 1),
('200604119', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이석호', '01000000034', '200604119@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 13, 1),
('200001296', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박아석', '01000000035', '200001296@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 13, 1);

-- 마케팅부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503227', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이진연', '01000000036', '202503227@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 14, 1),
('201811826', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박수연', '01000000037', '201811826@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 14, 1),
('201207525', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박연호', '01000000038', '201207525@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 14, 1),
('200604218', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정아은', '01000000039', '200604218@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 14, 1),
('200001822', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김석주', '01000000040', '200001822@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 14, 1),
('202503452', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최연민', '01000000041', '202503452@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 15, 1),
('201811877', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이은아', '01000000042', '201811877@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 15, 1),
('201207859', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최연진', '01000000043', '201207859@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 15, 1),
('200604481', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김진슬', '01000000044', '200604481@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 15, 1),
('200001528', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한진슬', '01000000045', '200001528@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 15, 1),
('202503854', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한민연', '01000000046', '202503854@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 16, 1),
('201811215', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김호민', '01000000047', '201811215@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 16, 1),
('201207260', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한하민', '01000000048', '201207260@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 16, 1),
('200604527', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이민우', '01000000049', '200604527@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 16, 1),
('200001403', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박석수', '01000000050', '200001403@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 16, 1),
('202503460', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조하석', '01000000051', '202503460@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 17, 1),
('201811542', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤연석', '01000000052', '201811542@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 17, 1),
('201207984', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박아하', '01000000053', '201207984@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 17, 1),
('200604909', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤경민', '01000000054', '200604909@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 17, 1),
('200001581', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최진연', '01000000055', '200001581@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 17, 1);

-- 재무/회계부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
    ('202503924', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이연호', '01000000056', '202503924@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 18, 1),
    ('201811108', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤슬진', '01000000057', '201811108@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 18, 1),
    ('201207121', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조민하', '01000000058', '201207121@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 18, 1),
    ('200604279', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정은경', '01000000059', '200604279@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 18, 1),
    ('200001386', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박은수', '01000000060', '200001386@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 18, 1),
    ('202503928', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '장우진', '01000000061', '202503928@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 19, 1),
    ('201811285', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조아수', '01000000062', '201811285@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 19, 1),
    ('201207410', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임우진', '01000000063', '201207410@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 19, 1),
    ('200604534', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임주석', '01000000064', '200604534@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 19, 1),
    ('200001876', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한하현', '01000000065', '200001876@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 19, 1),
    ('202503149', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김은석', '01000000066', '202503149@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울본사', 20, 1),
    ('201811564', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조석호', '01000000067', '201811564@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울본사', 20, 1),
    ('201207225', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박아은', '01000000068', '201207225@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울본사', 20, 1),
    ('200604991', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조아하', '01000000069', '200604991@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울본사', 20, 1),
    ('200001585', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한현연', '01000000070', '200001585@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울본사', 20, 1);

-- 생산부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503309', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임아수', '01000000071', '202503309@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울공장', 21, 1),
('201811556', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김현우', '01000000072', '201811556@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울공장', 21, 1),
('201207131', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한진주', '01000000073', '201207131@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울공장', 21, 1),
('200604408', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이진은', '01000000074', '200604408@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울공장', 21, 1),
('200001168', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최경우', '01000000075', '200001168@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울공장', 21, 1),
('202503720', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최진우', '01000000076', '202503720@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울공장', 22, 1),
('201811122', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임수진', '01000000077', '201811122@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울공장', 22, 1),
('201207188', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤수석', '01000000078', '201207188@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울공장', 22, 1),
('200604663', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정수하', '01000000079', '200604663@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울공장', 22, 1),
('200001865', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤아하', '01000000080', '200001865@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울공장', 22, 1),
('202503391', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이진주', '01000000081', '202503391@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울공장', 23, 1),
('201811114', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조현진', '01000000082', '201811114@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울공장', 23, 1),
('201207648', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한아수', '01000000083', '201207648@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울공장', 23, 1),
('200604365', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이슬민', '01000000084', '200604365@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울공장', 23, 1),
('200001946', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임연호', '01000000085', '200001946@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울공장', 23, 1),
('202503276', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조수호', '01000000086', '202503276@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울공장', 24, 1),
('201811117', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤슬연', '01000000087', '201811117@saladerp.com', '주임', '2018-11-15', NULL, FALSE, FALSE, '서울공장', 24, 1),
('201207379', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한주석', '01000000088', '201207379@saladerp.com', '대리', '2012-07-31', NULL, FALSE, FALSE, '서울공장', 24, 1),
('200604492', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이연진', '01000000089', '200604492@saladerp.com', '과장', '2006-04-16', NULL, FALSE, FALSE, '서울공장', 24, 1),
('200001745', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정진슬', '01000000090', '200001745@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울공장', 24, 1);

-- 연구개발부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503636', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤호호', '01000000091', '202503636@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울연구소', 25, 1),
('201411563', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최민주', '01000000092', '201411563@saladerp.com', '주임', '2014-11-06', NULL, FALSE, FALSE, '서울연구소', 25, 1),
('201311530', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '강수호', '01000000093', '201311530@saladerp.com', '대리', '2013-11-07', NULL, FALSE, FALSE, '서울연구소', 25, 1),
('201804340', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김호하', '01000000094', '201804340@saladerp.com', '과장', '2018-04-25', NULL, FALSE, FALSE, '서울연구소', 25, 1),
('200001199', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박수석', '01000000095', '200001199@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울연구소', 25, 1),
('202503987', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박슬주', '01000000096', '202503987@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울연구소', 26, 1),
('200305793', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최하진', '01000000097', '200305793@saladerp.com', '주임', '2003-05-16', NULL, FALSE, FALSE, '서울연구소', 26, 1),
('202303246', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조석슬', '01000000098', '202303246@saladerp.com', '대리', '2023-03-13', NULL, FALSE, FALSE, '서울연구소', 26, 1),
('201903532', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임수수', '01000000099', '201903532@saladerp.com', '과장', '2019-03-13', NULL, FALSE, FALSE, '서울연구소', 26, 1),
('200001254', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조석민', '01000000100', '200001254@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울연구소', 26, 1),
('202503571', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박연수', '01000000101', '202503571@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울연구소', 27, 1),
('200303937', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한주주', '01000000102', '200303937@saladerp.com', '주임', '2003-03-31', NULL, FALSE, FALSE, '서울연구소', 27, 1),
('201801108', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정연슬', '01000000103', '201801108@saladerp.com', '대리', '2018-01-30', NULL, FALSE, FALSE, '서울연구소', 27, 1),
('201611358', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이주연', '01000000104', '201611358@saladerp.com', '과장', '2016-11-28', NULL, FALSE, FALSE, '서울연구소', 27, 1),
('200001164', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조하슬', '01000000105', '200001164@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울연구소', 27, 1);

-- IT/정보시스템부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503540', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박수석', '01000000106', '202503540@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '판교IT센터', 28, 1),
('201109809', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤진수', '01000000107', '201109809@saladerp.com', '주임', '2011-09-01', NULL, FALSE, FALSE, '판교IT센터', 28, 1),
('200412486', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤진우', '01000000108', '200412486@saladerp.com', '대리', '2004-12-27', NULL, FALSE, FALSE, '판교IT센터', 28, 1),
('202106934', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박연진', '01000000109', '202106934@saladerp.com', '과장', '2021-06-22', NULL, FALSE, FALSE, '판교IT센터', 28, 1),
('200001395', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김주우', '01000000110', '200001395@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '판교IT센터', 28, 1),
('202503659', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최슬석', '01000000111', '202503659@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '판교IT센터', 29, 1),
('201501402', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한우석', '01000000112', '201501402@saladerp.com', '주임', '2015-01-17', NULL, FALSE, FALSE, '판교IT센터', 29, 1),
('200404963', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조슬우', '01000000113', '200404963@saladerp.com', '대리', '2004-04-14', NULL, FALSE, FALSE, '판교IT센터', 29, 1),
('200504814', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한연주', '01000000114', '200504814@saladerp.com', '과장', '2005-04-11', NULL, FALSE, FALSE, '판교IT센터', 29, 1),
('200001478', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조주민', '01000000115', '200001478@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '판교IT센터', 29, 1),
('202503576', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤호연', '01000000116', '202503576@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '판교IT센터', 30, 1),
('201002530', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조하민', '01000000117', '201002530@saladerp.com', '주임', '2010-02-12', NULL, FALSE, FALSE, '판교IT센터', 30, 1),
('202203996', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김진슬', '01000000118', '202203996@saladerp.com', '대리', '2022-03-20', NULL, FALSE, FALSE, '판교IT센터', 30, 1),
('201903383', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박호호', '01000000119', '201903383@saladerp.com', '과장', '2019-03-17', NULL, FALSE, FALSE, '판교IT센터', 30, 1),
('200001145', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤수하', '01000000120', '200001145@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '판교IT센터', 30, 1),
('202503458', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임호우', '01000000121', '202503458@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '판교IT센터', 31, 1),
('202302550', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조주연', '01000000122', '202302550@saladerp.com', '주임', '2023-02-13', NULL, FALSE, FALSE, '판교IT센터', 31, 1),
('201304601', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '강수연', '01000000123', '201304601@saladerp.com', '대리', '2013-04-26', NULL, FALSE, FALSE, '판교IT센터', 31, 1),
('201209148', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한석주', '01000000124', '201209148@saladerp.com', '과장', '2012-09-30', NULL, FALSE, FALSE, '판교IT센터', 31, 1),
('200001560', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이수수', '01000000125', '200001560@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '판교IT센터', 31, 1);

-- 전략기획부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503392', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤호주', '01000000126', '202503392@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '본사전략부', 32, 1),
('201702585', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최수호', '01000000127', '201702585@saladerp.com', '주임', '2017-02-03', NULL, FALSE, FALSE, '본사전략부', 32, 1),
('201611786', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한연우', '01000000128', '201611786@saladerp.com', '대리', '2016-11-28', NULL, FALSE, FALSE, '본사전략부', 32, 1),
('201503151', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최민하', '01000000129', '201503151@saladerp.com', '과장', '2015-03-08', NULL, FALSE, FALSE, '본사전략부', 32, 1),
('200001705', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한슬호', '01000000130', '200001705@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '본사전략부', 32, 1),
('202503844', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '강주주', '01000000131', '202503844@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '본사전략부', 33, 1),
('201701733', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한슬주', '01000000132', '201701733@saladerp.com', '주임', '2017-01-05', NULL, FALSE, FALSE, '본사전략부', 33, 1),
('200408388', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최우수', '01000000133', '200408388@saladerp.com', '대리', '2004-08-14', NULL, FALSE, FALSE, '본사전략부', 33, 1),
('200706391', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조민주', '01000000134', '200706391@saladerp.com', '과장', '2007-06-06', NULL, FALSE, FALSE, '본사전략부', 33, 1),
('200001743', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김우하', '01000000135', '200001743@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '본사전략부', 33, 1),
('202503298', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤연석', '01000000136', '202503298@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '본사전략부', 34, 1),
('201704544', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임슬우', '01000000137', '201704544@saladerp.com', '주임', '2017-04-21', NULL, FALSE, FALSE, '본사전략부', 34, 1),
('201305897', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '이민민', '01000000138', '201305897@saladerp.com', '대리', '2013-05-08', NULL, FALSE, FALSE, '본사전략부', 34, 1),
('200107983', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한주하', '01000000139', '200107983@saladerp.com', '과장', '2001-07-16', NULL, FALSE, FALSE, '본사전략부', 34, 1),
('200001134', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최석석', '01000000140', '200001134@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '본사전략부', 34, 1);

-- 법무부
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503658', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '김슬호', '01000000141', '202503658@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울법무실', 35, 1),
('202406207', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조진석', '01000000142', '202406207@saladerp.com', '주임', '2024-06-11', NULL, FALSE, FALSE, '서울법무실', 35, 1),
('200106375', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '조석주', '01000000143', '200106375@saladerp.com', '대리', '2001-06-01', NULL, FALSE, FALSE, '서울법무실', 35, 1),
('202111355', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박수석', '01000000144', '202111355@saladerp.com', '과장', '2021-11-08', NULL, FALSE, FALSE, '서울법무실', 35, 1),
('200001475', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '최주주', '01000000145', '200001475@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울법무실', 35, 1),
('202503143', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '한민석', '01000000146', '202503143@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '서울법무실', 36, 1),
('202301251', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '정슬우', '01000000147', '202301251@saladerp.com', '주임', '2023-01-08', NULL, FALSE, FALSE, '서울법무실', 36, 1),
('201912371', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤호민', '01000000148', '201912371@saladerp.com', '대리', '2019-12-27', NULL, FALSE, FALSE, '서울법무실', 36, 1),
('201405695', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '임슬수', '01000000149', '201405695@saladerp.com', '과장', '2014-05-28', NULL, FALSE, FALSE, '서울법무실', 36, 1),
('200001216', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '강연호', '01000000150', '200001216@saladerp.com', '팀장', '2000-01-01', NULL, FALSE, FALSE, '서울법무실', 36, 1);

-- 미배정
INSERT INTO employee (code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted, work_place, department_id, profile) VALUES
('202503986', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '윤슬우', '01000000151', '202503986@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '미배정', 1, 1),
('202503758', '$2a$10$a20vES08g9Y66UpTd5FQDeqC10eBvivO1jmTFGy/txQzFJ9Vywbyq', '박진주', '01000000152', '202503758@saladerp.com', '사원', '2025-03-01', NULL, FALSE, FALSE, '미배정', 1, 1);

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
     , ('TV', '2024 QLED 4K QDE1 (189 cm) + 3.1 ch 사운드바 B650D', 'KQ75QDE1-B6', 'SAM-TV-KQ75QDE1-B6', '삼성', 3369000, 67425, '삼성 QLED TV', FALSE, 1)
     , ('세탁기', '비스포크 그랑데 AI 세탁기', 'WF24B7600KW', 'SAM-WASH-WF24B7600KW', '삼성', 1890000, 37900, 'AI 기능을 탑재한 삼성 세탁기', FALSE, 2)
     , ('건조기', 'LG 트롬 듀얼 인버터 히트펌프', 'RH18VNA', 'LG-DRY-RH18VNA', 'LG', 1720000, 35500, '에너지 효율이 뛰어난 LG 건조기', FALSE, 2)
     , ('에어컨', '휘센 듀얼 에어컨 2in1', 'FQ18DADWE2', 'LG-AC-FQ18DADWE2', 'LG', 2490000, 46800, '여름 필수품, LG 휘센 에어컨', FALSE, 3)
     , ('청소기', '제트 무선 청소기 VS20A956B', 'VS20A956B', 'SAM-VAC-VS20A956B', '삼성', 1340000, 27900, '강력한 흡입력의 삼성 무선 청소기', FALSE, 3)
     , ('오븐', '비스포크 큐커 AI 오븐', 'MC32A7035KT', 'SAM-OVEN-MC32A7035KT', '삼성', 590000, 12500, '다기능 삼성 오븐', FALSE, 4)
     , ('식기세척기', '디오스 식기세척기 스팀', 'DUBJ1G', 'LG-DISH-DUBJ1G', 'LG', 1380000, 28900, '고온 스팀 살균 LG 식기세척기', FALSE, 4)
     , ('냉장고', '비스포크 냉장고 4도어', 'RF85T9111AP', 'SAM-REF-RF85T9111AP', '삼성', 3100000, 58900, '고급형 삼성 냉장고', FALSE, 5)
     , ('TV', 'LG OLED evo C3 65인치', 'OLED65C3KNA', 'LG-TV-OLED65C3KNA', 'LG', 2790000, 55300, '고화질 LG OLED TV', FALSE, 5)
     , ('세탁기', 'LG 트윈워시 세탁기', 'F21VDP', 'LG-WASH-F21VDP', 'LG', 1980000, 39200, '세탁과 탈수를 동시에 LG 트윈워시', FALSE, 6)
     , ('건조기', '삼성 AI 건조기', 'DV90T8240SH', 'SAM-DRY-DV90T8240SH', '삼성', 1790000, 36500, 'AI 제어 삼성 건조기', FALSE, 6)
     , ('에어컨', '무풍에어컨 클래식', 'AF17A6474TZ', 'SAM-AC-AF17A6474TZ', '삼성', 2650000, 49500, '무풍 냉방 삼성 에어컨', FALSE, 7)
     , ('청소기', '코드제로 A9S', 'A958VA', 'LG-VAC-A958VA', 'LG', 1190000, 24500, 'LG 코드제로 무선 청소기', FALSE, 7)
     , ('오븐', 'LG 광파오븐 MA324B', 'MA324B', 'LG-OVEN-MA324B', 'LG', 540000, 11900, '간편한 요리를 위한 LG 오븐', FALSE, 8)
     , ('식기세척기', '삼성 비스포크 식기세척기', 'DW60A8055UG', 'SAM-DISH-DW60A8055UG', '삼성', 1450000, 29900, '스마트 기능 탑재 식기세척기', FALSE, 8)
     , ('공기청정기', 'LG 퓨리케어 360° 공기청정기', 'AS351NNFA', 'LG-AP-AS351NNFA', 'LG', 1040000, 20900, '360도 청정 기능 LG 공기청정기', FALSE, 9)
     , ('공기청정기', '삼성 블루스카이 5000', 'AX60A5510WDD', 'SAM-AP-AX60A5510WDD', '삼성', 980000, 19900, '초미세먼지 제거 삼성 공기청정기', FALSE, 9)
     , ('전자레인지', 'LG 전자레인지 MW25S', 'MW25S', 'LG-MIW-MW25S', 'LG', 210000, 4900, '간단 요리를 위한 전자레인지', FALSE, 10)
     , ('제습기', '삼성 무풍 제습기', 'AY10R5171', 'SAM-DEH-AY10R5171', '삼성', 670000, 13900, '쾌적한 여름을 위한 삼성 제습기', FALSE, 10);

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
VALUES (1, '문의 제목 1', '이 제품은 어떤 기능이 있나요?', '2025-05-02 06:54:50.537606', '완료', '답변 드리겠습니다.', False, 2),
       (2, '문의 제목 2', '이 제품은 어떤 기능이 있나요?', '2025-05-04 12:21:07.537610', '대기', NULL, False, 3),
       (3, '문의 제목 3', '이 제품은 어떤 기능이 있나요?', '2025-05-08 01:02:51.537612', '완료', '답변 드리겠습니다.', False, 4),
       (4, '문의 제목 4', '이 제품은 어떤 기능이 있나요?', '2025-05-10 22:06:49.537613', '대기', NULL, False, 5),
       (5, '문의 제목 5', '이 제품은 어떤 기능이 있나요?', '2025-05-13 10:19:10.537615', '완료', '답변 드리겠습니다.', False, 6),
       (6, '문의 제목 6', '이 제품은 어떤 기능이 있나요?', '2025-05-16 23:24:34.537616', '대기', NULL, False, 7),
       (7, '문의 제목 7', '이 제품은 어떤 기능이 있나요?', '2025-05-20 06:11:17.537618', '완료', '답변 드리겠습니다.', False, 1);

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
# INSERT INTO APPROVAL (id, code, title, content, req_date, aprv_date, state, comment, req_id, aprv_id, contract_id)
# VALUES (1, 'APR2024001', '승인 요청 제목1', '승인 요청 내용1', '2025-05-01 23:48:33.467055', '2025-05-01 23:48:33.467056', '반려',
#         '코멘트1', 2, 3, 1),
#        (2, 'APR2024002', '승인 요청 제목2', '승인 요청 내용2', '2025-05-04 18:38:04.467058', '2025-05-04 18:38:04.467059', '승인',
#         '코멘트2', 3, 4, 2),
#        (3, 'APR2024003', '승인 요청 제목3', '승인 요청 내용3', '2025-05-07 20:34:51.467061', '2025-05-07 20:34:51.467061', '반려',
#         '코멘트3', 4, 5, 3),
#        (4, 'APR2024004', '승인 요청 제목4', '승인 요청 내용4', '2025-05-11 00:29:46.467062', '2025-05-11 00:29:46.467063', '승인',
#         '코멘트4', 5, 6, 4),
#        (5, 'APR2024005', '승인 요청 제목5', '승인 요청 내용5', '2025-05-13 21:16:18.467064', '2025-05-13 21:16:18.467065', '반려',
#         '코멘트5', 6, 7, 5),
#        (6, 'APR2024006', '승인 요청 제목6', '승인 요청 내용6', '2025-05-28 02:14:09.467066', '2025-05-28 02:14:09.467067', '승인',
#         '코멘트6', 7, 1, 6),
#        (7, 'APR2024007', '승인 요청 제목7', '승인 요청 내용7', '2025-05-28 02:14:09.467068', '2025-05-28 02:14:09.467068', '반려',
#         '코멘트7', 1, 2, 7);

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

-- 알림 더미 데이터 (결재 알림)
# INSERT INTO NOTIFICATION (type, content, created_at, url, is_read, is_deleted, employee_id)
# VALUES
#     ('APPROVAL', '[결재 요청] A-2506-0001 계약에 대한 승인을 요청했습니다.', NOW(), 'http://www.naver.com', FALSE, FALSE, 1),
#     ('APPROVAL', '[결재 요청] A-2506-0002 계약에 대한 승인을 요청했습니다.', NOW(), 'http://www.naver.com', FALSE, FALSE, 1),
#     ('APPROVAL', '[결재 요청] A-2506-0003 계약에 대한 승인을 요청받았습니다.', NOW(), 'http://www.naver.com', FALSE, FALSE, 5);

UPDATE product
SET name = '제습기',
    company = '삼성',
    serial_number = 'SN20240001'
WHERE id = 6;

UPDATE product
SET name = '전자레인지',
    company = 'LG',
    serial_number = 'SN20240002'
WHERE id = 1;

UPDATE product
SET name = '공기 청정기',
    company = '삼성',
    serial_number = 'SN20240003'
WHERE id = 2;

COMMIT;
