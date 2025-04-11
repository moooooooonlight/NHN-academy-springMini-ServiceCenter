package com.nhnacademy.nhnmartservicecenter.controller;

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
 * 사용자가 CUSTOM일 경우 Logic을 담당하는 Controller
 */
@Slf4j
@Controller
@RequestMapping("/custom")
public class CustomInquiryController {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/";
    private final InquiryRegisterValidation validation;

    /**
     * 생성자 : 의존성 주입
     * @param inquiryRepository : 문의사항 정보 저장소
     * @param userRepository : 사용자 정보 저장소
     * @param validation : 문의사항 등록 유효성 검사객체
     */
    public CustomInquiryController(InquiryRepository inquiryRepository, UserRepository userRepository, InquiryRegisterValidation validation) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
        this.validation = validation;
    }

    @InitBinder("inquiryRegisterValidation")
    protected void initBinder(WebDataBinder binder){
        binder.addValidators(validation);
    }


    /**
     * @param model  : 문의 카테고리 리스트, 문의사항 리스트 포함
     * @param category  : 검색된 문의사항 카테고리
     * @return : 문의사항 리스트 보여주기
     */
    @GetMapping("/cs")
    public String getInquiryList(Model model,
                                 @RequestParam(value = "category", required = false) String category){
        List<Inquiry> inquiryList = inquiryRepository.getInquiryList();
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());
        if(!Objects.isNull(category) && !category.isEmpty()){
            inquiryList = filterInquiryList(category, inquiryList);
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
    @GetMapping(value = "/cs", params = "my=yes")
    public String getMyInquiryList(Model model,
                                   HttpServletRequest request){
        List<Inquiry> inquiryList = inquiryRepository.getInquiryList();
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());
        String userId = (String) request.getSession().getAttribute("userId");
        List<Inquiry> myInquiryList = new ArrayList<>();
        for(Inquiry inquiry : inquiryList){
            if (inquiry.getUserId().equals(userId)) {
                myInquiryList.add(inquiry);
            }
        }
        Collections.sort(inquiryList);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("inquiryList", myInquiryList);
        return "customHome";
    }


    /**
     * @param model : 카테고리 리스트 포함
     * @return : 상품 등록 폼 띄우기
     */
    @GetMapping("/cs/inquiry")
    public String registerInquiry(Model model){
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());

        model.addAttribute("categoryList", categoryList);
        return "inquiryRegisterForm";
    }


    /**
     * @param inquiryRegisterRequest : 문의사항 폼 정보 얻기
     * @param file :
     * @param request : request 세션 값을 통해 사용자 정보 얻기
     * @param model :
     * @return : 문의사항 등록 수행
     * @throws IOException
     */
    @PostMapping("/cs/inquiry")
    public String doRegisterInquiry(@Validated @ModelAttribute InquiryRegisterRequest inquiryRegisterRequest,
                                    BindingResult bindingResult,
                                    @RequestParam("file") MultipartFile file,
                                    HttpServletRequest request,
                                    Model model) throws IOException {
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        String userId = (String) request.getSession().getAttribute("userId");
        User user = userRepository.getUser(userId);

        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        file.transferTo(Paths.get(filePath));
//        String fileUrl = "/uploads/" + file.getOriginalFilename();


        Inquiry inquiry = new Inquiry(
                                userId,
                                user.getName(),
                                inquiryRegisterRequest.getTitle(),
                                inquiryRegisterRequest.getContent(),
                                inquiryRegisterRequest.getCategory(),
                                filePath);
        inquiryRepository.register(inquiry);

        return "redirect:/custom/cs";
    }


    /**
     * @param model : 사용자 정보 및 문의사항 정보 포함
     * @param inquiryId : 문의사항 Id를 통해 값 가져오기
     * @param request : request 세션의 사용자 정보 얻기
     * @return : 문의사항 세부정보 띄우기
     */
    @GetMapping("/cs/inquiry/{inquiryId}")
    public String getInquiry(Model model,
                             @PathVariable("inquiryId") long inquiryId,
                             HttpServletRequest request){
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        String userId = (String) request.getSession().getAttribute("userId");
        User user = userRepository.getUser(userId);

        model.addAttribute("userName", user.getName());
        model.addAttribute("inquiry", inquiry);
        return "inquiryDetail";
    }


    /**
     * @param category : 필터 적용할 category 정보.
     * @param inquiryList : 문의사항 리스트 전체
     * @return : category 검색 구현 메소드
     */
    private static List<Inquiry> filterInquiryList(String category, List<Inquiry> inquiryList) {
        List<Inquiry> filterInquiryList = new ArrayList<>();
        for(Inquiry inquiry : inquiryList){
            if(String.valueOf(inquiry.getCategory()).equals(category)){
                filterInquiryList.add(inquiry);
            }
        }
        inquiryList = filterInquiryList;
        return inquiryList;
    }
}
