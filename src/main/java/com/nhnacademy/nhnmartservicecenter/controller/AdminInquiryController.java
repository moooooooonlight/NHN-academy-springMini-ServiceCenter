package com.nhnacademy.nhnmartservicecenter.controller;

import com.nhnacademy.nhnmartservicecenter.domain.Answer;
import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminInquiryController {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    public AdminInquiryController(InquiryRepository inquiryRepository, UserRepository userRepository) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
    }


    /**
     * 관리자 페이지 처리
     * @param model : inquiry 인자값 전달용
     * @return : 관리자 main 페이지
     */
    @GetMapping("/cs")
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


    @GetMapping("/cs/inquiry/{inquiryId}")
    public String getAnswerFormInquiry(Model model,
                             @PathVariable("inquiryId") long inquiryId,
                             HttpServletRequest request){
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user = userRepository.getUser(userId);

        model.addAttribute("csName", user.getName());
        model.addAttribute("inquiry", inquiry);
        return "inquiryAnswer";
    }


    @PostMapping("/cs/inquiry/{inquiryId}")
    public String answerToInquiry(@PathVariable("inquiryId") long inquiryId,
                                  @RequestParam String answerContent,
                                  HttpServletRequest request){
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        String userId = (String) request.getSession().getAttribute("userId");
        User user = userRepository.getUser(userId);
        Answer answer = new Answer(answerContent, user);
        inquiry.setAnswer(answer);
        inquiryRepository.updateInquiry(inquiryId, inquiry);

        return "redirect:/admin/cs";
    }
}
