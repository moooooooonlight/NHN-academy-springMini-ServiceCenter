package com.nhnacademy.nhnmartservicecenter.controller.admin;

import com.nhnacademy.nhnmartservicecenter.domain.Answer;
import com.nhnacademy.nhnmartservicecenter.domain.AnswerRequest;
import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.exception.ValidationFailedException;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import com.nhnacademy.nhnmartservicecenter.validation.InquiryAnswerValidation;
import com.nhnacademy.nhnmartservicecenter.validation.InquiryRegisterValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


/**
 * 사용자 권한이 ADMIN 일 경우 문의사항 Logic을 담당하는 Controller
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminAnswerController {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final InquiryAnswerValidation validation;


    public AdminAnswerController(InquiryRepository inquiryRepository, UserRepository userRepository, InquiryAnswerValidation validation) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
        this.validation = validation;
    }

    @InitBinder("inquiryRegisterValidation")
    protected void initBinder(WebDataBinder binder){
        binder.addValidators(validation);
    }

    @ModelAttribute("inquiry")
    public Inquiry getInquiry(@PathVariable("inquiryId") long inquiryId){
        return inquiryRepository.getInquiry(inquiryId);
    }




    /**
     * 문의사항 답변을 위한 폼 띄우기
     * @param model : 관리자 이름, 문의사항 정보 포함
     * @param request : request 세션의 사용자 정보 얻기
     * @return : 관리자 답변 폼
     */
    @GetMapping("/cs/inquiry/{inquiryId}")
    public String getAnswerFormInquiry(Model model,
                                       HttpServletRequest request){
        Inquiry inquiry = (Inquiry) model.getAttribute("inquiry");
        User user = getUser(request);

        model.addAttribute("csName", user.getName());
        model.addAttribute("inquiry", inquiry);
        return "inquiryAnswer";
    }


    /**
     * 답변 등록 처리
     * @param answerContent : 답변 내용 request
     * @param bindingResult : validate 결과 값
     * @param request : request 세션의 사용자 정보 얻기
     * @return : 관리자 main 페이지
     */
    @PostMapping("/cs/inquiry/{inquiryId}")
    public String answerToInquiry(Model model,
                                  @Validated @RequestParam AnswerRequest answerContent,
                                  BindingResult bindingResult,
                                  HttpServletRequest request){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }


        Inquiry inquiry = (Inquiry) model.getAttribute("inquiry");
        User user = getUser(request);
        Answer answer = new Answer(answerContent.getContent(), user);
        inquiry.setAnswer(answer);
        inquiryRepository.updateInquiry(inquiry.getId(), inquiry);

        return "redirect:/admin/cs";
    }



    private User getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user = userRepository.getUser(userId);
        return user;
    }
}
