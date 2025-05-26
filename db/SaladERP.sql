-- saladdb 계정으로 실행!
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS APPROVAL;
DROP TABLE IF EXISTS CONTRACT_PRODUCT;
DROP TABLE IF EXISTS SALES;
DROP TABLE IF EXISTS CONTRACT;
DROP TABLE IF EXISTS EMPLOYEE_GOAL;
DROP TABLE IF EXISTS EMPLOYEE_NOTICE;
DROP TABLE IF EXISTS DEFAULT_GOAL;
DROP TABLE IF EXISTS DOCUMENT_ORIGIN;
DROP TABLE IF EXISTS DOCUMENT_TEMPLATE;
DROP TABLE IF EXISTS CONSULTATION;
DROP TABLE IF EXISTS CUSTOMER;
DROP TABLE IF EXISTS DEPARTMENT_PERFORMANCE;
DROP TABLE IF EXISTS QNA;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS NOTICE;
DROP TABLE IF EXISTS EMPLOYEE_PERFORMANCE;
DROP TABLE IF EXISTS EMPLOYEE;
DROP TABLE IF EXISTS FILE_UPLOAD;
DROP TABLE IF EXISTS DEPARTMENT;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE DEPARTMENT
(
    id          INT         NOT NULL,
    name        VARCHAR(10) NOT NULL,
    is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    sup_dept_id INT         NULL,
    CONSTRAINT PK_DEPARTMENT PRIMARY KEY (id),
    CONSTRAINT FK_DEPARTMENT_TO_DEPARTMENT
        FOREIGN KEY (sup_dept_id)
            REFERENCES DEPARTMENT (id)
);

CREATE TABLE FILE_UPLOAD
(
    id          INT         NOT NULL,
    origin_file VARCHAR(20) NOT NULL,
    rename_file VARCHAR(20) NOT NULL,
    path        VARCHAR(50) NOT NULL,
    created_at  DATETIME    NOT NULL,
    type        VARCHAR(3)  NOT NULL COMMENT '프로필, 상품, 계약서',
    CONSTRAINT PK_FILE_UPLOAD PRIMARY KEY (id)
);

CREATE TABLE EMPLOYEE
(
    id            INT         NOT NULL,
    code          INT         NOT NULL COMMENT '입사년월(6)+번호(3), UNIQUE',
    password      VARCHAR(20) NOT NULL,
    name          VARCHAR(10) NOT NULL,
    phone         VARCHAR(11) NOT NULL COMMENT '01012345678 형태로 저장',
    level         VARCHAR(3)  NOT NULL DEFAULT '사원' COMMENT '사원, 주임, 대리, 과장, 팀장, 관리자',
    hire_date     DATE        NULL,
    resign_date   DATE        NULL,
    is_deleted    BOOLEAN     NOT NULL DEFAULT FALSE,
    work_place    VARCHAR(10) NULL,
    department_id INT         NOT NULL DEFAULT 1 COMMENT '팀 미배정시(동작구1팀 등 )1번 미배정팀 배정',
    profile       INT         NOT NULL,
    CONSTRAINT PK_EMPLOYEE PRIMARY KEY (id),
    CONSTRAINT FK_DEPARTMENT_TO_EMPLOYEE
        FOREIGN KEY (department_id)
            REFERENCES DEPARTMENT (id),
    CONSTRAINT FK_FILE_UPLOAD_TO_EMPLOYEE
        FOREIGN KEY (profile)
            REFERENCES FILE_UPLOAD (id)
);

CREATE TABLE EMPLOYEE_PERFORMANCE
(
    id                      INT           NOT NULL,
    rental_product_count    INT           NULL,
    rental_retention_rate   INT           NULL,
    new_customer_count      INT           NULL,
    total_rental_amount     BIGINT        NULL,
    customer_feedback_score DECIMAL(2, 1) NULL,
    target_date             INT(6)        NOT NULL COMMENT '202505',
    employee_id             INT           NOT NULL,
    CONSTRAINT PK_EMPLOYEE_PERFORMANCE PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_EMPLOYEE_PERFORMANCE
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id)
);

CREATE TABLE NOTICE
(
    id          INT         NOT NULL,
    title       VARCHAR(20) NOT NULL,
    content     TEXT        NOT NULL,
    created_at  DATETIME    NOT NULL COMMENT '2025-01-01 00:00:00',
    is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    employee_id INT         NOT NULL,
    CONSTRAINT PK_NOTICE PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_NOTICE
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id)
);

CREATE TABLE PRODUCT
(
    id             INT          NOT NULL,
    category       VARCHAR(30)  NOT NULL,
    name           VARCHAR(255) NOT NULL,
    serial_number  VARCHAR(255) NOT NULL,
    product_code   VARCHAR(255) NOT NULL COMMENT '회사 내부 부여',
    company        VARCHAR(255) NOT NULL,
    origin_cost    INT          NOT NULL,
    rental_cost    INT          NOT NULL,
    description    TEXT         NULL,
    is_deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    file_upload_id INT          NOT NULL,
    CONSTRAINT PK_PRODUCT PRIMARY KEY (id),
    CONSTRAINT FK_FILE_UPLOAD_TO_PRODUCT
        FOREIGN KEY (file_upload_id)
            REFERENCES FILE_UPLOAD (id)
);

CREATE TABLE QNA
(
    id             INT         NOT NULL,
    title          VARCHAR(20) NOT NULL,
    content        TEXT        NOT NULL,
    created_at     DATETIME    NOT NULL,
    answer_status  VARCHAR(20) NOT NULL DEFAULT '미답변',
    answer_content TEXT        NULL,
    is_deleted     BOOLEAN     NOT NULL DEFAULT FALSE,
    employee_id    INT         NOT NULL,
    CONSTRAINT PK_QNA PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_QNA
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id)
);

CREATE TABLE DEPARTMENT_PERFORMANCE
(
    id                      INT           NOT NULL,
    rental_product_count    INT           NULL,
    rental_retention_rate   INT           NULL,
    new_customer_count      INT           NULL,
    total_rental_amount     BIGINT        NULL,
    customer_feedback_score DECIMAL(2, 1) NULL,
    target_date             INT(6)        NOT NULL COMMENT '202505',
    department_id           INT           NOT NULL,
    CONSTRAINT PK_DEPARTMENT_PERFORMANCE PRIMARY KEY (id),
    CONSTRAINT FK_DEPARTMENT_TO_DEPARTMENT_PERFORMANCE
        FOREIGN KEY (department_id)
            REFERENCES DEPARTMENT (id)
);

CREATE TABLE CUSTOMER
(
    id          INT          NOT NULL,
    name        VARCHAR(20)  NOT NULL,
    birthdate   VARCHAR(20)  NOT NULL COMMENT '2025-01-01',
    address     VARCHAR(20)  NULL,
    phone       VARCHAR(11)  NOT NULL COMMENT '01012345678',
    email       VARCHAR(255) NULL,
    register_at DATE         NULL,
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
    type        VARCHAR(20)  NOT NULL COMMENT '리드, 고객 ENUM으로 관리',
    etc         VARCHAR(20)  NULL,
    CONSTRAINT PK_CUSTOMER PRIMARY KEY (id)
);

CREATE TABLE CONSULTATION
(
    id             INT           NOT NULL,
    consult_at     DATETIME      NOT NULL,
    content        TEXT          NOT NULL,
    is_deleted     BOOLEAN       NOT NULL DEFAULT FALSE,
    etc            VARCHAR(20)   NULL,
    feedback_score DECIMAL(2, 1) NULL,
    employee_id    INT           NOT NULL,
    customer_id    INT           NOT NULL,
    CONSTRAINT PK_CONSULTATION PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_CONSULTATION
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id),
    CONSTRAINT FK_CUSTOMER_TO_CONSULTATION
        FOREIGN KEY (customer_id)
            REFERENCES CUSTOMER (id)
);

CREATE TABLE DOCUMENT_TEMPLATE
(
    id             INT         NOT NULL,
    name           VARCHAR(20) NOT NULL,
    version        VARCHAR(5)  NOT NULL,
    description    VARCHAR(20) NULL,
    created_at     DATETIME    NOT NULL,
    is_deleted     BOOLEAN     NOT NULL DEFAULT FALSE,
    file_upload_id INT         NOT NULL,
    CONSTRAINT PK_DOCUMENT_TEMPLATE PRIMARY KEY (id),
    CONSTRAINT FK_FILE_UPLOAD_TO_DOCUMENT_TEMPLATE
        FOREIGN KEY (file_upload_id)
            REFERENCES FILE_UPLOAD (id)
);

CREATE TABLE DOCUMENT_ORIGIN
(
    id                   INT      NOT NULL,
    created_at           DATETIME NOT NULL,
    is_deleted           BOOLEAN  NULL DEFAULT FALSE,
    document_template_id INT      NOT NULL,
    file_upload_id       INT      NOT NULL,
    CONSTRAINT PK_DOCUMENT_ORIGIN PRIMARY KEY (id),
    CONSTRAINT FK_DOCUMENT_TEMPLATE_TO_DOCUMENT_ORIGIN
        FOREIGN KEY (document_template_id)
            REFERENCES DOCUMENT_TEMPLATE (id),
    CONSTRAINT FK_FILE_UPLOAD_TO_DOCUMENT_ORIGIN
        FOREIGN KEY (file_upload_id)
            REFERENCES FILE_UPLOAD (id)
);

CREATE TABLE DEFAULT_GOAL
(
    id                      INT           NOT NULL,
    level                   VARCHAR(20)   NOT NULL,
    rental_product_count    INT           NOT NULL,
    rental_retention_rate   INT           NOT NULL,
    new_customer_count      INT           NOT NULL,
    total_rental_amount     BIGINT        NOT NULL,
    customer_feedback_score DECIMAL(2, 1) NOT NULL,
    target_year             INT(4)        NOT NULL COMMENT '2025',
    CONSTRAINT PK_DEFAULT_GOAL PRIMARY KEY (id)
);

CREATE TABLE EMPLOYEE_NOTICE
(
    id          INT     NOT NULL,
    is_checked  BOOLEAN NOT NULL DEFAULT FALSE,
    employee_id INT     NOT NULL,
    notice_id   INT     NOT NULL,
    CONSTRAINT PK_EMPLOYEE_NOTICE PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_EMPLOYEE_NOTICE
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id),
    CONSTRAINT FK_NOTICE_TO_EMPLOYEE_NOTICE
        FOREIGN KEY (notice_id)
            REFERENCES NOTICE (id)
);

CREATE TABLE EMPLOYEE_GOAL
(
    id                      INT           NOT NULL,
    rental_product_count    INT           NOT NULL,
    rental_retention_rate   INT           NOT NULL,
    new_customer_count      INT           NOT NULL,
    total_rental_amount     BIGINT        NOT NULL,
    customer_feedback_score DECIMAL(2, 1) NOT NULL,
    target_date             INT(6)        NOT NULL COMMENT '202505',
    employee_id             INT           NOT NULL,
    CONSTRAINT PK_EMPLOYEE_GOAL PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_EMPLOYEE_GOAL
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id)
);

CREATE TABLE CONTRACT
(
    id                 INT         NOT NULL,
    code               VARCHAR(11) NOT NULL COMMENT 'C-YYMM-nnnn (C-2501-0001)',
    created_at         DATETIME    NOT NULL,
    start_date         DATE        NOT NULL,
    end_date           DATE        NOT NULL,
    status             VARCHAR(10) NOT NULL COMMENT '결재 전, 반려, 결재 중, 계약 중, 계약 만료, 중도 해지',
    amount             INT         NOT NULL,
    bank_name          VARCHAR(20) NOT NULL,
    bank_account       VARCHAR(20) NOT NULL COMMENT '- 없이 숫자만 저장',
    payment_day        INT         NOT NULL,
    deposit_owner      VARCHAR(20) NOT NULL,
    relationship       VARCHAR(20) NOT NULL COMMENT '본인, 부모, 자녀',
    payment_email      VARCHAR(20) NOT NULL,
    is_deleted         BOOLEAN     NOT NULL DEFAULT FALSE,
    etc                VARCHAR(20) NULL,
    document_origin_id INT         NOT NULL,
    customer_id        INT         NOT NULL,
    employee_id        INT         NOT NULL,
    CONSTRAINT PK_CONTRACT PRIMARY KEY (id),
    CONSTRAINT FK_DOCUMENT_ORIGIN_TO_CONTRACT
        FOREIGN KEY (document_origin_id)
            REFERENCES DOCUMENT_ORIGIN (id),
    CONSTRAINT FK_CUSTOMER_TO_CONTRACT
        FOREIGN KEY (customer_id)
            REFERENCES CUSTOMER (id),
    CONSTRAINT FK_EMPLOYEE_TO_CONTRACT
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id)
);

CREATE TABLE SALES
(
    id            INT         NOT NULL,
    sales_date    DATE        NOT NULL,
    department    VARCHAR(20) NOT NULL,
    employee_name VARCHAR(20) NOT NULL,
    amount        INT         NOT NULL,
    is_deleted    INT         NOT NULL DEFAULT FALSE,
    contract_id   INT         NOT NULL,
    CONSTRAINT PK_SALES PRIMARY KEY (id),
    CONSTRAINT FK_CONTRACT_TO_SALES
        FOREIGN KEY (contract_id)
            REFERENCES CONTRACT (id)
);

CREATE TABLE CONTRACT_PRODUCT
(
    id          INT NOT NULL,
    quantity    INT NOT NULL,
    contract_id INT NOT NULL,
    product_id  INT NOT NULL,
    CONSTRAINT PK_CONTRACT_PRODUCT PRIMARY KEY (id),
    CONSTRAINT FK_CONTRACT_TO_CONTRACT_PRODUCT
        FOREIGN KEY (contract_id)
            REFERENCES CONTRACT (id),
    CONSTRAINT FK_PRODUCT_TO_CONTRACT_PRODUCT
        FOREIGN KEY (product_id)
            REFERENCES PRODUCT (id)
);

CREATE TABLE APPROVAL
(
    id          INT         NOT NULL,
    code        VARCHAR(11) NOT NULL COMMENT 'A-YYMM-nnnn (A-2501-0001)',
    title       VARCHAR(20) NOT NULL COMMENT '필요?',
    content     TEXT        NOT NULL,
    req_date    DATETIME    NOT NULL,
    aprv_date   DATETIME    NULL,
    state       VARCHAR(20) NOT NULL COMMENT '요청됨, 승인됨, 반려됨',
    comment     VARCHAR(20) NULL COMMENT '결재 반려 시 사유',
    req_id      INT         NOT NULL,
    aprv_id     INT         NOT NULL,
    contract_id INT         NOT NULL,
    CONSTRAINT PK_APPROVAL PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_APPROVAL_REQ
        FOREIGN KEY (req_id)
            REFERENCES EMPLOYEE (id),
    CONSTRAINT FK_EMPLOYEE_TO_APPROVAL_APRV
        FOREIGN KEY (aprv_id)
            REFERENCES EMPLOYEE (id),
    CONSTRAINT FK_CONTRACT_TO_APPROVAL
        FOREIGN KEY (contract_id)
            REFERENCES CONTRACT (id)
);
