package com.nhnacademy.nhnmartservicecenter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationFailedException.class)
    public String handleValidationFailedException(ValidationFailedException ex, Model model) {
        String errorMessage = "";
        if (!Objects.isNull(ex.getBindingResult())) {
            BindingResult bindingResult = ex.getBindingResult();
            errorMessage = bindingResult.getAllErrors()
                            .stream()
                            .map(error -> new StringBuilder().append("ObjectName=").append(error.getObjectName())
                                    .append(",Message=").append(error.getDefaultMessage())
                                    .append(",code=").append(error.getCode()))
                            .collect(Collectors.joining(" | "));
        }

        if (!Objects.isNull(ex.getErrorMessage())) {
            errorMessage = ex.getErrorMessage();
        }


        model.addAttribute("message", errorMessage);
        model.addAttribute("exception", ex);
        return "error";
    }

    @ExceptionHandler(UserNotLoginException.class)
    public String handleUserNotLoginException(UserNotLoginException ex, Model model){
        model.addAttribute("message","로그인이 필요합니다.");
        model.addAttribute("exception", ex);
        return "error";
    }

    @ExceptionHandler(UserRegisterAlreadyExistException.class)
    public String handleUserRegisterAlreadyExistException(UserRegisterAlreadyExistException ex, Model model) {
        model.addAttribute("message","이미 존재하는 ID 입니다.");
        model.addAttribute("exception", ex);
        return "error";
    }
    @ExceptionHandler(UserNotFountException.class)
    public String handleUserNotFountException(UserNotFountException ex, Model model) {
        model.addAttribute("message","존재하지 않는 ID 입니다.");
        model.addAttribute("exception", ex);
        return "error";
    }
    @ExceptionHandler(InquiryNotFoundException.class)
    public String handleInquiryNotFoundException(InquiryNotFoundException ex, Model model) {
        model.addAttribute("message","존재하지 않는 문의사항 입니다.");
        model.addAttribute("exception", ex);
        return "error";
    }
}
