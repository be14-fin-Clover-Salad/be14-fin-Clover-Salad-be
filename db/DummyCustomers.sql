USE saladdb;

/* 고객 더미 데이터 */
INSERT INTO customer (
  name, birthdate, address, phone, email,
  register_at, is_deleted, type, etc
) VALUES
('김석희', '1999-06-18', '서울특별시 성북구 길음동', '01019990618', 'lovesh618@gmail.com', '2024-12-30', FALSE, '리드', NULL),
('최혜민', '1998-04-07', '경기도 성남시 분당구', '01029920911', 'chm0407@gmail.com', '2024-12-30', FALSE, '고객', NULL),
('곽우석', '1999-12-31', '서울특별시 동작구 상도1동', '01019991231', 'dntjr8106@gmail.com', '2024-12-30', FALSE, '리드', NULL),
('이서영', '', '서울특별시 강남구 수서동', '01096221456', 'seoyounglee@hwsystem.com', '2025-05-09', FALSE, '리드', NULL),
('팜하니', '2004-10-06', '', '01004100609', 'hanni@newjeans.kr', '2025-04-16', FALSE, '리드', NULL),
('김민지', '2004-05-07', '강원도 춘천시 중앙로', '01004050711', 'minji@newjeans.kr', '2025-05-19', FALSE, '리드', NULL),
('강해린', '2006-05-15', '서울시 서초구 반포대로', '01006051512', 'haerin@newjeans.kr', '2025-05-22', FALSE, '리드', NULL),
('이혜인', '2008-04-21', '인천시 연수구 송도동', '01008042113', 'haein@newjeans.kr', '2025-06-01', FALSE, '고객', NULL),
('신하람', '2007-10-17', '서울시 강남구 압구정로', '01007101714', 'haram@babymonster.kr', '2025-06-02', FALSE, '리드', NULL),
('이다인', '2008-08-05', '서울시 용산구 이태원동', '01008080515', 'rora@babymonster.kr', '2025-06-03', FALSE, '고객', NULL),
('정아현', '2006-04-11', '서울시 마포구 연남동', '01006041116', 'ahyeon@babymonster.kr', '2025-06-04', FALSE, '리드', NULL),
('아사', '2006-04-17', '일본 도쿄도 오가사와라무라', '01006041717', 'asa@babymonster.kr', '2025-06-05', FALSE, '고객', NULL),
('루카', '2002-03-20', '일본 오사카시', '01002032018', 'ruka@babymonster.kr', '2025-06-06', FALSE, '고객', NULL),
('파리타', '2005-08-26', '태국 방콕시', '01005082619', 'pharita@babymonster.kr', '2025-06-07', FALSE, '리드', NULL),
('치키타', '2009-02-17', '태국 치앙마이시', '01009021720', 'chiquita@babymonster.kr', '2025-06-08', FALSE, '고객', NULL);