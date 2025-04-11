package com.nhnacademy.nhnmartservicecenter.controller;

import com.nhnacademy.nhnmartservicecenter.domain.Answer;
import com.nhnacademy.nhnmartservicecenter.domain.AnswerRequest;
import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.exception.ValidationFailedException;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 사용자 권한이 ADMIN일 경우 Logic을 담당하는 Controller
 */
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


    /**
     * 문의사항 답변을 위한 폼 띄우기
     * @param model : 관리자 이름, 문의사항 정보 포함
     * @param inquiryId : 선택한 문의사항의 Id값
     * @param request : request 세션의 사용자 정보 얻기
     * @return : 관리자 답변 폼
     */
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


    /**
     * 답변 등록 처리
     * @param inquiryId : 선택한 문의사항의 Id값
     * @param answerContent : 답변 내용 request
     * @param bindingResult : validate 결과 값
     * @param request : request 세션의 사용자 정보 얻기
     * @return : 관리자 main 페이지
     */
    @PostMapping("/cs/inquiry/{inquiryId}")
    public String answerToInquiry(@PathVariable("inquiryId") long inquiryId,
                                  @Validated @RequestParam AnswerRequest answerContent,
                                  BindingResult bindingResult,
                                  HttpServletRequest request){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        String userId = (String) request.getSession().getAttribute("userId");
        User user = userRepository.getUser(userId);
        Answer answer = new Answer(answerContent.getContent(), user);
        inquiry.setAnswer(answer);
        inquiryRepository.updateInquiry(inquiryId, inquiry);

        return "redirect:/admin/cs";
    }
}
