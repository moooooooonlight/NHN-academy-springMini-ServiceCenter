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
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/custom")
public class CustomerInquiryRegisterController {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "target/classes/static/upload/";

    private final InquiryRegisterValidation validation;

    public CustomerInquiryRegisterController(InquiryRepository inquiryRepository, UserRepository userRepository, InquiryRegisterValidation validation) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
        this.validation = validation;
    }

    @InitBinder("inquiryRegisterValidation")
    protected void initBinder(WebDataBinder binder){
        binder.addValidators(validation);
    }




    /**
     * @param model : 카테고리 리스트 포함
     * @return : 문의사항 등록 폼 띄우기
     */
    @GetMapping("/cs/inquiry")
    public String registerInquiry(Model model){
        List<Inquiry.category> categoryList = List.of(Inquiry.category.values());

        model.addAttribute("categoryList", categoryList);
        return "inquiryRegisterForm";
    }


    /**
     * @param inquiryRegisterRequest : 문의사항 폼 정보 얻기
     * @param files : 업로드된 파일 List
     * @param request : request 세션 값을 통해 사용자 정보 얻기
     * @param model :
     * @return : 문의사항 등록 수행
     * @throws IOException
     */
    @PostMapping("/cs/inquiry")
    public String doRegisterInquiry(@Validated @ModelAttribute InquiryRegisterRequest inquiryRegisterRequest,
                                    BindingResult bindingResult,
                                    @RequestParam("files") MultipartFile[] files,                                    HttpServletRequest request,
                                    Model model) throws IOException {
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        String userId = (String) request.getSession().getAttribute("userId");
        User user = userRepository.getUser(userId);

        List<String> imagePathList = new ArrayList<>();


        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filePath = UPLOAD_DIR + file.getOriginalFilename();
                file.transferTo(Paths.get(filePath));

                String urlPath = "/upload/" + file.getOriginalFilename();
                imagePathList.add(urlPath);
            }
        }

        Inquiry inquiry = new Inquiry(
                userId,
                user.getName(),
                inquiryRegisterRequest.getTitle(),
                inquiryRegisterRequest.getContent(),
                inquiryRegisterRequest.getCategory(),
                imagePathList);
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

}
