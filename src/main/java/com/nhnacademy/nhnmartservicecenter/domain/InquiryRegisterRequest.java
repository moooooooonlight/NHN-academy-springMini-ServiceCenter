package com.nhnacademy.nhnmartservicecenter.domain;

import lombok.Value;

@Value
public class InquiryRegisterRequest {
    String title;
    String content;
    Inquiry.category category;
}
