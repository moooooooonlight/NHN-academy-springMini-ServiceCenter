package com.nhnacademy.nhnmartservicecenter.controller;

import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.domain.InquiryRegisterRequest;
import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/custom")
public class CustomInquiryController {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/";


    public CustomInquiryController(InquiryRepository inquiryRepository, UserRepository userRepository) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
    }


    /**
     *     문의사항 리스트 보여주기
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
     *    문의사항 등록 폼 띄우기
     */
    @GetMapping("/cs/inquiry")
    public String registerInquiry(Model model){
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());

        model.addAttribute("categoryList", categoryList);
        return "inquiryRegisterForm";
    }


    /**
     * 문의사항 등록 수행.
     */
    @PostMapping("/cs/inquiry")
    public String doRegisterInquiry(@ModelAttribute InquiryRegisterRequest inquiryRegisterRequest,
                                    @RequestParam("file") MultipartFile file,
                                    HttpServletRequest request,
                                    Model model) throws IOException {
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

        return "redirect:/custom/cs/inquiry";
    }


    /**
     * 문의사항 세부사항
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
     * category 검색 구현 메소드
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
