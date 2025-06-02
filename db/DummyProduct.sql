USE saladdb;

/* 설명. 임시 파일 더미데이터 */
INSERT INTO file_upload (origin_file, rename_file, path, created_at, type)
VALUES ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd')
     , ('asdf', 'asdf', 'asdf', '2025-05-26 23:34:38', 'asd');

/* 설명. 상품 더미데이터 */
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