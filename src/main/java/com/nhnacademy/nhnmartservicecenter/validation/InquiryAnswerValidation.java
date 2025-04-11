package com.nhnacademy.nhnmartservicecenter.validation;

import com.nhnacademy.nhnmartservicecenter.domain.AnswerRequest;
import com.nhnacademy.nhnmartservicecenter.exception.ValidationFailedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class InquiryAnswerValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AnswerRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AnswerRequest request = (AnswerRequest) target;

        String content = request.getContent();
        if(content.length()<1 && content.length()>40000){
            throw new ValidationFailedException("답변은 1~40000자 이어야합니다.");
        }
    }
}
