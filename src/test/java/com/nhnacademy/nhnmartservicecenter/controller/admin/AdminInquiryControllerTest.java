package com.nhnacademy.nhnmartservicecenter.controller.admin;

import com.nhnacademy.nhnmartservicecenter.domain.Inquiry;
import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.repository.InquiryRepository;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminInquiryController.class)
class AdminInquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryRepository inquiryRepository;

    @MockitoBean
    private UserRepository userRepository;

    private Inquiry testInquiry = new Inquiry("testUserId","testUserName","test", "test", Inquiry.category.Complain,null);


    @BeforeEach
    void setUp() {
        inquiryRepository = Mockito.mock(InquiryRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminInquiryController(inquiryRepository)).build();
    }

    @Test
    @Order(1)
    @DisplayName("답변 안된 문의사항 리스트 화면 띄우기")
    void getInquiryList() throws Exception {
        // given
        when(inquiryRepository.getInquiryList()).thenReturn(List.of(testInquiry));
        when(userRepository.getUser(anyString())).thenReturn(new User("testUserId", "testUserPassword", "testUserName", User.auth.ADMIN));

        // when
        mockMvc.perform(get("/admin/cs")
                        .param("category", String.valueOf(testInquiry.getCategory())))
                .andExpect(status().isOk())
                .andExpect(view().name("adminHome"))
                .andReturn();

        // then
        verify(inquiryRepository, times(1)).getInquiryList();
    }
}
