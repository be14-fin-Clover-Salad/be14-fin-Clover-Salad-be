-- saladdb 계정으로 실행!
USE saladdb;

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
    id          INT         NOT NULL AUTO_INCREMENT,
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
    id          INT         NOT NULL AUTO_INCREMENT,
    origin_file VARCHAR(20) NOT NULL,
    rename_file VARCHAR(20) NOT NULL,
    path        VARCHAR(50) NOT NULL,
    created_at  DATETIME    NOT NULL,
    type        VARCHAR(3)  NOT NULL,
    CONSTRAINT PK_FILE_UPLOAD PRIMARY KEY (id)
);

CREATE TABLE EMPLOYEE
(
    id            INT         NOT NULL AUTO_INCREMENT,
    code          INT         NOT NULL,
    password      VARCHAR(20) NOT NULL,
    name          VARCHAR(10) NOT NULL,
    phone         VARCHAR(11) NOT NULL,
    level         VARCHAR(3)  NOT NULL DEFAULT '사원',
    hire_date     DATE        NULL,
    resign_date   DATE        NULL,
    is_deleted    BOOLEAN     NOT NULL DEFAULT FALSE,
    work_place    VARCHAR(10) NULL,
    department_id INT         NOT NULL DEFAULT 1,
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
    id                      INT           NOT NULL AUTO_INCREMENT,
    rental_product_count    INT           NULL,
    rental_retention_count  INT           NULL,
    total_rental_count      INT           NULL,
    new_customer_count      INT           NULL,
    total_rental_amount     BIGINT        NULL,
    customer_feedback_score DECIMAL(2, 1) NULL,
    customer_feedback_count INT           NULL,
    target_date             INT(6)        NOT NULL,
    employee_id             INT           NOT NULL,
    CONSTRAINT PK_EMPLOYEE_PERFORMANCE PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_EMPLOYEE_PERFORMANCE
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id),
    CONSTRAINT UQ_TARGET_DATE_EMPLOYEE_ID
        UNIQUE (target_date, employee_id)
);

CREATE TABLE NOTICE
(
    id          INT         NOT NULL AUTO_INCREMENT,
    title       VARCHAR(20) NOT NULL,
    content     TEXT        NOT NULL,
    created_at  DATETIME    NOT NULL,
    is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    employee_id INT         NOT NULL,
    CONSTRAINT PK_NOTICE PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_NOTICE
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id)
);

CREATE TABLE PRODUCT
(
    id             INT          NOT NULL AUTO_INCREMENT,
    category       VARCHAR(30)  NOT NULL,
    name           VARCHAR(255) NOT NULL,
    serial_number  VARCHAR(255) NOT NULL,
    product_code   VARCHAR(255) NOT NULL UNIQUE,
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
    id             INT         NOT NULL AUTO_INCREMENT,
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
    id                      INT           NOT NULL AUTO_INCREMENT,
    rental_product_count    INT           NULL,
    rental_retention_count  INT           NULL,
    total_rental_count      INT           NULL,
    new_customer_count      INT           NULL,
    total_rental_amount     BIGINT        NULL,
    customer_feedback_score DECIMAL(2, 1) NULL,
    customer_feedback_count INT           NULL,
    target_date             INT(6)        NOT NULL,
    department_id           INT           NOT NULL,
    CONSTRAINT PK_DEPARTMENT_PERFORMANCE PRIMARY KEY (id),
    CONSTRAINT FK_DEPARTMENT_TO_DEPARTMENT_PERFORMANCE
        FOREIGN KEY (department_id)
            REFERENCES DEPARTMENT (id),
    CONSTRAINT UQ_TARGET_DATE_DEPARTMENT_ID
        UNIQUE (target_date, department_id)
);

CREATE TABLE CUSTOMER
(
    id          INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(20)  NOT NULL,
    birthdate   VARCHAR(20)  NULL,
    address     VARCHAR(20)  NULL,
    phone       VARCHAR(11)  NOT NULL,
    email       VARCHAR(255) NULL,
    register_at DATE         NULL,
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
    type        VARCHAR(20)  NOT NULL,
    etc         VARCHAR(20)  NULL,
    CONSTRAINT PK_CUSTOMER PRIMARY KEY (id)
);

CREATE TABLE CONSULTATION
(
    id             INT           NOT NULL AUTO_INCREMENT,
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
    id             INT         NOT NULL AUTO_INCREMENT,
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
    id                   INT      NOT NULL AUTO_INCREMENT,
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
    id                      INT           NOT NULL AUTO_INCREMENT,
    level                   VARCHAR(20)   NOT NULL,
    rental_product_count    INT           NOT NULL,
    rental_retention_rate   INT           NOT NULL,
    new_customer_count      INT           NOT NULL,
    total_rental_amount     BIGINT        NOT NULL,
    customer_feedback_score DECIMAL(2, 1) NOT NULL,
    target_year             INT(4)        NOT NULL,
    CONSTRAINT PK_DEFAULT_GOAL PRIMARY KEY (id)
);

CREATE TABLE EMPLOYEE_NOTICE
(
    id          INT     NOT NULL AUTO_INCREMENT,
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
    id                      INT           NOT NULL AUTO_INCREMENT,
    rental_product_count    INT           NOT NULL,
    rental_retention_count  INT           NOT NULL,
    total_rental_count      INT           NOT NULL,
    new_customer_count      INT           NOT NULL,
    total_rental_amount     BIGINT        NOT NULL,
    customer_feedback_score DECIMAL(2, 1) NOT NULL,
    customer_feedback_count INT           NOT NULL,
    target_date             INT(6)        NOT NULL,
    employee_id             INT           NOT NULL,
    CONSTRAINT PK_EMPLOYEE_GOAL PRIMARY KEY (id),
    CONSTRAINT FK_EMPLOYEE_TO_EMPLOYEE_GOAL
        FOREIGN KEY (employee_id)
            REFERENCES EMPLOYEE (id),
    CONSTRAINT UQ_TARGET_DATE_EMPLOYEE_ID
        UNIQUE (target_date, employee_id)
);

CREATE TABLE CONTRACT
(
    id                 INT         NOT NULL AUTO_INCREMENT,
    code               VARCHAR(11) NOT NULL,
    created_at         DATETIME    NOT NULL,
    start_date         DATE        NOT NULL,
    end_date           DATE        NOT NULL,
    status             VARCHAR(10) NOT NULL,
    amount             INT         NOT NULL,
    bank_name          VARCHAR(20) NOT NULL,
    bank_account       VARCHAR(20) NOT NULL,
    payment_day        INT         NOT NULL,
    deposit_owner      VARCHAR(20) NOT NULL,
    relationship       VARCHAR(20) NOT NULL,
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
    id            INT         NOT NULL AUTO_INCREMENT,
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
    id          INT NOT NULL AUTO_INCREMENT,
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
    id          INT         NOT NULL AUTO_INCREMENT,
    code        VARCHAR(11) NOT NULL,
    title       VARCHAR(20) NOT NULL,
    content     TEXT        NOT NULL,
    req_date    DATETIME    NOT NULL,
    aprv_date   DATETIME    NULL,
    state       VARCHAR(20) NOT NULL,
    comment     VARCHAR(20) NULL,
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
