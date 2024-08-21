package com.tbread.facechat.domain.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Result<T> {
    private final String message;
    private final HttpStatus status;
    private final T data;
    private boolean success;

    public Result(String message, HttpStatus status, T data, boolean success) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.success = success;
    }

    public Result(String message, int code, T data, boolean success) {
        this.message = message;
        this.status = HttpStatus.valueOf(code);
        this.data = data;
        this.success = success;
    }

    public Result(String message, HttpStatus status, boolean success) {
        this.message = message;
        this.status = status;
        this.data = null;
        this.success = success;
    }

    public Result(String message, int code, boolean success) {
        this.message = message;
        this.status = HttpStatus.valueOf(code);
        this.data = null;
        this.success = success;
    }


    public Result(HttpStatus status, T data, boolean success) {
        if (success) {
            this.message = "요청에 성공했습니다.";
        } else {
            this.message = "요청에 실패했습니다.";
        }
        this.status = status;
        this.data = data;
        this.success = success;
    }


    public Result(HttpStatus status, boolean success) {
        if (success) {
            this.message = "요청에 성공했습니다.";
        } else {
            this.message = "요청에 실패했습니다.";
        }
        this.status = status;
        this.data = null;
        this.success = success;
    }

    public ResponseEntity<Result<T>> publish(){
        return ResponseEntity.status(this.status).body(this);
    }
}
