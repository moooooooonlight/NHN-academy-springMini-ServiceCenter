package com.nhnacademy.nhnmartservicecenter.repository;

import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;

import java.util.List;

public interface InquiryRepository {
    boolean exist(long id);
    Inquiry register(Inquiry inquiry);
    List<Inquiry> getInquiryList();

    Inquiry getInquiry(long id);

    void updateInquiry(long id,Inquiry inquiry);

    void deleteInquiry(long id);
}
