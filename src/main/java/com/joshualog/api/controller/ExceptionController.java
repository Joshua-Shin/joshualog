package com.joshualog.api.controller;

import com.joshualog.api.exception.JologException;
import com.joshualog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
// 예외가 발생하면 얘가 잡아줌
public class ExceptionController {
    // 어떤 예외를 잡아서, 어떤 응답 메시지를 보낼것인가에 대한 함수
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();
        for(FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }
    @ExceptionHandler(JologException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> postNotFoundHandler(JologException e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(e.getStatus()))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();
        ResponseEntity<ErrorResponse> response = ResponseEntity.status(e.getStatus()).body(body);
        return response;
    }
}
