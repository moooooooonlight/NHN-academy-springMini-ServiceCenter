package com.nhnacademy.nhnmartservicecenter.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationFailedException extends RuntimeException{
    private String errorMessage;
    private BindingResult bindingResult;
    public ValidationFailedException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public ValidationFailedException(String message) {
        this.errorMessage = message;
    }
}
