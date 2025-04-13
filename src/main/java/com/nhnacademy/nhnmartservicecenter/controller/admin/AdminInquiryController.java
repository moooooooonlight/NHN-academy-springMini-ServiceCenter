package com.nhnacademy.nhnmartservicecenter.controller.admin;

import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 사용자 권한이 ADMIN일 경우 Main Logic을 담당하는 Controller
 */
@Slf4j
@Controller
public class AdminInquiryController {
    private final InquiryRepository inquiryRepository;

    public AdminInquiryController(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    /**
     * 관리자 페이지 처리
     * @param model : inquiry 인자값 전달용
     * @return : 관리자 main 페이지
     */
    @GetMapping("/admin/cs")
    public String getInquiryList(Model model,
                                 @RequestParam(value = "category", required = false) String category){
        List<Inquiry> inquiryList = inquiryRepository.getInquiryList();
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());

        List<Inquiry> noAnswerInquiryList = new ArrayList<>();
        for(Inquiry inquiry : inquiryList){
            if(inquiry.getAnswer()!=null){
                continue;
            }
            if(Objects.isNull(category) || category.isEmpty()){
                noAnswerInquiryList.add(inquiry);
                continue;
            }
            if(String.valueOf(inquiry.getCategory()).equals(category)){
                noAnswerInquiryList.add(inquiry);
            }
        }

        Collections.sort(noAnswerInquiryList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("inquiryList", noAnswerInquiryList);
        return "adminHome";
    }
}
