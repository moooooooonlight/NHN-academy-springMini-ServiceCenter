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
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/custom")
public class CustomInquiryController {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "src/main/resources/file";


    public CustomInquiryController(InquiryRepository inquiryRepository, UserRepository userRepository) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
    }


    // 문의사항 등록 폼 띄우기
    @GetMapping("/cs/inquiry")
    public String registerInquiry(Model model){
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());

        model.addAttribute("category", categoryList);
        return "inquiryRegisterForm";
    }


    // 문의사항 등록 수행.
    @PostMapping("/cs/inquiry")
    public String doRegisterInquiry(@ModelAttribute InquiryRegisterRequest inquiryRegisterRequest,
                                    @RequestParam("file") MultipartFile file,
                                    HttpServletRequest request,
                                    Model model) throws IOException {
        String studentId = (String) request.getSession().getAttribute("studentId");
        User user = userRepository.getUser(studentId);

        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        file.transferTo(Paths.get(filePath));
        Inquiry inquiry = new Inquiry(
                studentId,
                inquiryRegisterRequest.getTitle(),
                inquiryRegisterRequest.getContent(),
                inquiryRegisterRequest.getCategory(),
                filePath);
        inquiryRepository.register(inquiry);

        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("userName", user.getName());
        model.addAttribute("inquiry", inquiry);
        return "inquiryDetail";
    }


    // 문의사항 리스트 보여주기
    @GetMapping("/cs")
    public String getInquiryList(Model model){
        List<Inquiry> inquiryList = inquiryRepository.getInquiryList();

        model.addAttribute("inquiryList", inquiryList);
        return "customHome";
    }


    // 문의사항 세부사항
    @GetMapping("/cs/inquiry/{inquiryId}")
    public String getInquiry(Model model, @PathVariable("inquiryId") long inquiryId){
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        model.addAttribute("inquiry", inquiry);
        return "inquiryDetail";
    }
}
