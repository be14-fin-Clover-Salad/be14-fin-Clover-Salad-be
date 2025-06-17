package com.clover.salad.common.exception;

public class CustomersException {

    /** 설명. 고객 접근 권한이 없을 때 */
    public static class CustomerAccessDeniedException extends RuntimeException {
        public CustomerAccessDeniedException(String message) {
            super(message);
        }
    }

    /** 설명. 고객을 찾을 수 없을 때 */
    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(String message) {
            super(message);
        }
    }

    /** 설명. 잘못된 고객 데이터일 때(예: 유효성 실패) */
    public static class InvalidCustomerDataException extends RuntimeException {
        public InvalidCustomerDataException(String message) {
            super(message);
        }
    }
}
