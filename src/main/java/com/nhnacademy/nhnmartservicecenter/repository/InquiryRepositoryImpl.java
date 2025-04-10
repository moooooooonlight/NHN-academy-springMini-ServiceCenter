package com.nhnacademy.nhnmartservicecenter.repository;

import com.nhnacademy.nhnmartservicecenter.domain.Answer;
import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.domain.User;
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
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"444","444","접근에러","접근이 안되요", Inquiry.category.ETC,null,new Answer("빠른 조치를 취하겠습니다", new User("123","123","123", User.auth.ADMIN))));
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"555","555","재료 소진","재료 소진", Inquiry.category.Suggestion,null,null));
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"666","666","주차 불편","주차 불편", Inquiry.category.Complain,null,null));
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"777","777","계산 오류","계산 오류", Inquiry.category.Complain,null,null));
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"888","888","칭찬합니다","칭찬합니다", Inquiry.category.Praise,null,null));
        inquiryMap.put(inquiryCount,new Inquiry(inquiryCount++,"999","999","카트가 부족해요","카트가 부족해요", Inquiry.category.Complain,null,null));
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
