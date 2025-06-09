USE saladdb;


INSERT INTO department (id, name, is_deleted, sup_dept_id)
VALUES (1, '영업부', FALSE, NULL);


INSERT INTO file_upload (id, origin_file, rename_file, path, created_at, type)
VALUES (1, 'contract_original.pdf', 'contract_renamed.pdf', '/uploads', NOW(), 'PDF');


INSERT INTO employee (id, code, password, name, phone, email, level, hire_date, resign_date, is_admin, is_deleted,
                      work_place, department_id, profile)
VALUES (1, 'EMP001', 'hashedpw123', '김사원', '01012345678', 'kim@example.com', '사원', '2023-01-01', NULL, FALSE, FALSE,
        '서울', 1, 1);


INSERT INTO employee_performance (id, rental_product_count, rental_retention_count, total_rental_count,
                                  new_customer_count, total_rental_amount, customer_feedback_score,
                                  customer_feedback_count, target_date, employee_id)
VALUES (1, 5, 3, 8, 2, 1500000, 4.5, 10, 202406, 1);


INSERT INTO notice (id, title, content, created_at, is_deleted, employee_id)
VALUES (1, '중요 공지', '계약 시스템 점검 예정입니다.', NOW(), FALSE, 1);


INSERT INTO product (id, category, name, serial_number, product_code, company, origin_cost, rental_cost, description,
                     is_deleted, file_upload_id)
VALUES (1, '가전', '벽걸이형 에어컨', 'SN-AC-001', 'P-AC001', '삼성전자', 1000000, 50000, '벽걸이형 에어컨, 냉방전용', FALSE, 1),
       (2, '가전', '청소기', 'SN-CL-002', 'P-CL002', 'LG전자', 450000, 30000, '무선 청소기, 강력한 흡입력', FALSE, 1),
       (3, '가전', '정수기', 'SN-WT-003', 'P-WT003', '코웨이', 850000, 40000, '냉온수 가능 정수기', FALSE, 1);


INSERT INTO qna (id, title, content, created_at, answer_status, answer_content, is_deleted, employee_id)
VALUES (1, '계약 관련 질문', '계약서 수정은 어떻게 하나요?', NOW(), '미답변', NULL, FALSE, 1);


INSERT INTO department_performance (id, rental_product_count, rental_retention_count, total_rental_count,
                                    new_customer_count, total_rental_amount, customer_feedback_score,
                                    customer_feedback_count, target_date, department_id)
VALUES (1, 15, 10, 25, 5, 4500000, 4.7, 20, 202406, 1);


INSERT INTO customer (id, name, birthdate, address, phone, email, register_at, is_deleted, type, etc)
VALUES (1, '홍길동', '1990-01-01', '서울시 강남구', '01011112222', 'hong@example.com', '2024-06-01', FALSE, '개인', NULL);


INSERT INTO consultation (id, consult_at, content, is_deleted, etc, feedback_score, employee_id, customer_id)
VALUES (1, NOW(), '렌탈 계약 상담', FALSE, NULL, 4.5, 1, 1);


INSERT INTO document_template (id, name, version, description, created_at, is_deleted, file_upload_id)
VALUES (1, '기본 계약서', 'v1.0', '기본 렌탈 계약서 양식', NOW(), FALSE, 1);


INSERT INTO document_origin (id, created_at, is_deleted, document_template_id, file_upload_id)
VALUES (1, NOW(), FALSE, 1, 1);


INSERT INTO default_goal (id, level, rental_product_count, rental_retention_rate, new_customer_count,
                          total_rental_amount, customer_feedback_score, target_year)
VALUES (1, '사원', 10, 80, 5, 3000000, 4.5, 2025);


INSERT INTO employee_notice (id, is_checked, employee_id, notice_id)
VALUES (1, FALSE, 1, 1);


INSERT INTO employee_goal (id, rental_product_count, rental_retention_count, total_rental_count, new_customer_count,
                           total_rental_amount, customer_feedback_score, customer_feedback_count, target_date,
                           employee_id)
VALUES (1, 10, 5, 15, 3, 2500000, 4.6, 12, 202406, 1);


INSERT INTO contract (id, code, created_at, start_date, end_date, status, amount, bank_name, bank_account, payment_day,
                      deposit_owner, relationship, payment_email, is_deleted, etc, document_origin_id, customer_id,
                      employee_id)
VALUES (1, 'C-20240601', NOW(), '2025-06-01', '2026-06-01', '진행중', 1500000, '국민은행', '123456-78-901234', 15, '홍길동', '본인',
        'hong@pay.com', FALSE, NULL, 1, 1, 1);


INSERT INTO sales (id, sales_date, department, employee_name, amount, is_deleted, contract_id)
VALUES (1, '2025-06-01', '영업부', '김사원', 1500000, 0, 1);


INSERT INTO contract_product (id, quantity, contract_id, product_id)
VALUES (1, 1, 1, 1),
       (2, 2, 1, 2),
       (3, 1, 1, 3);

INSERT INTO approval (id, code, title, content, req_date, aprv_date, state, comment, req_id, aprv_id, contract_id)
VALUES (1, 'A-20240601', '계약서 검토', '계약 내용을 확인해 주세요.', NOW(), NULL, '대기', NULL, 1, 1, 1);
