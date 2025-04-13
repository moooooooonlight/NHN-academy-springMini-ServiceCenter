package com.nhnacademy.nhnmartservicecenter.controller.custom;

import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.domain.InquiryRegisterRequest;
import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.exception.ValidationFailedException;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import com.nhnacademy.nhnmartservicecenter.validation.InquiryRegisterValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 사용자가 CUSTOM일 경우 Main Logic을 담당하는 Controller
 */
@Slf4j
@Controller
public class CustomInquiryController {
    private final InquiryRepository inquiryRepository;

    public CustomInquiryController(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }


    /**
     * @param model  : 문의 카테고리 리스트, 문의사항 리스트 포함
     * @param category  : 검색된 문의사항 카테고리
     * @return : 문의사항 리스트 보여주기
     */
    @GetMapping("/custom/cs")
    public String getInquiryList(Model model,
                                 @RequestParam(value = "category", required = false) String category){
        List<Inquiry> inquiryList = inquiryRepository.getInquiryList();
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());
        if(!Objects.isNull(category) && !category.isEmpty()){
            inquiryList = filteCategoryInquiryList(category, inquiryList);
        }
        Collections.sort(inquiryList);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("inquiryList", inquiryList);
        return "customHome";
    }



    /**
     * @param model : 카테고리 리스트, 현재 사용자가 등록한 문의사항 리스트 포함
     * @param request  : request 세션에 등록된 사용자 정보 얻기
     * @return  : 현재 사용자가 등록한 문의사항만 존재하는 리스트 화면 띄우기
     */
    @GetMapping(value = "/custom/cs", params = "my=yes")
    public String getMyInquiryList(Model model,
                                   HttpServletRequest request){
        List<Inquiry> inquiryList = inquiryRepository.getInquiryList();
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());
        String userId = (String) request.getSession().getAttribute("userId");
        List<Inquiry> myInquiryList = filteMyInquiryList(userId, inquiryList);
        Collections.sort(inquiryList);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("inquiryList", myInquiryList);
        return "customHome";
    }





    private static List<Inquiry> filteMyInquiryList( String userId, List<Inquiry> inquiryList) {
        List<Inquiry> filterInquiryList = new ArrayList<>();
        for(Inquiry inquiry : inquiryList){
            if (inquiry.getUserId().equals(userId)) {
                filterInquiryList.add(inquiry);
            }
        }
        return filterInquiryList;
    }

    private static List<Inquiry> filteCategoryInquiryList(String category, List<Inquiry> inquiryList) {
        List<Inquiry> filterInquiryList = new ArrayList<>();
        for(Inquiry inquiry : inquiryList){
            if(String.valueOf(inquiry.getCategory()).equals(category)){
                filterInquiryList.add(inquiry);
            }
        }
        return filterInquiryList;
    }
}
