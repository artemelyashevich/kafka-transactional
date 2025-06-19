package com.elyashevich.producer.controller;

import com.elyashevich.producer.dto.ErrorDto;
import com.elyashevich.producer.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KafkaTransactionExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleTransactionAborted(BusinessException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorDto(e.getMessage())
        );
    }
}