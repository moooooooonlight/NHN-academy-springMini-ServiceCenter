package com.nhnacademy.nhnmartservicecenter.validation;

import com.nhnacademy.nhnmartservicecenter.domain.InquiryRegisterRequest;
import com.nhnacademy.nhnmartservicecenter.exception.ValidationFailedException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InquiryRegisterValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return InquiryRegisterRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InquiryRegisterRequest request = (InquiryRegisterRequest) target;

        String title = request.getTitle();
        if(title.length()<2 && title.length()>200){
            throw new ValidationFailedException("제목 길이는 2~200자 이어야합니다.");
        }

        String content = request.getContent();
        if(content.length()<0 && content.length()>40000){
            throw new ValidationFailedException("문의 내용은 0~40000자 이어야합니다.");
        }
    }
}
