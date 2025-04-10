package com.nhnacademy.nhnmartservicecenter.repository;

import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InquiryRepositoryImpl implements InquiryRepository{

    Map<Long, Inquiry> inquiryMap;
    private long inquiryCount = 1;

    public InquiryRepositoryImpl(){
        inquiryMap = new ConcurrentHashMap<>(); // ConcurrentHashMap의 장점 이유?
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"456","접근에러","접근이 안되요", Inquiry.category.Complain,null));
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"456","포인트추가","포인트 적립해주세요", Inquiry.category.Suggestion,null));
    }

    @Override
    public boolean exist(long id) {
        return inquiryMap.containsKey(id);
    }

    @Override
    public Inquiry register(Inquiry inquiry) {
        inquiry.setId(inquiryCount);
        return inquiryMap.put(inquiryCount++,inquiry);
    }

    @Override
    public List<Inquiry> getInquiryList() {
        Collection<Inquiry> values = inquiryMap.values();
        return new ArrayList<>(values);
    }

    @Override
    public Inquiry getInquiry(long id) {
        if(!exist(id)){
            // 존재하지 않는 문의사항입니다.
        }

        return inquiryMap.get(id);
    }

    @Override
    public void updateInquiry(long id,Inquiry inquiry) {
        if(!exist(id)){
            // 존재하지 않는 문의사항입니다.
        }

        inquiryMap.put(id,inquiry);
    }

    @Override
    public void deleteInquiry(long id) {
        if(!exist(id)){
            // 존재하지 않는 문의사항입니다.
        }

        inquiryMap.remove(id);
    }
}
