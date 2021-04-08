package com.eunhye.api.advice;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
    // private static final serialVersionUID =1L;
    private HttpStatus status;
    private String message;

    public RestException(int status, String message) {
        this.status = HttpStatus.valueOf(status); // 입력한 상태코드를 httpstatus 로 변경
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
}
