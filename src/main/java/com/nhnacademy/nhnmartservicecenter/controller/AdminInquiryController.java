package com.nhnacademy.nhnmartservicecenter.controller;

import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminInquiryController {
    private final InquiryRepository repository;

    public AdminInquiryController(InquiryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cs/inquiry")
    public String getInquiryList(Model model){
        List<Inquiry> inquiryList = repository.getInquiryList();
        model.addAttribute("inquiryList", inquiryList);

        return "customHome";
    }

    @PostMapping("/cs/inquiry/{inquiryId}")
    public String getInquiry(Model model,
                             @PathVariable("inquiryId") long inquiryId){
        Inquiry inquiry = repository.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiry);

        return "inquiryDetail";
    }
}
