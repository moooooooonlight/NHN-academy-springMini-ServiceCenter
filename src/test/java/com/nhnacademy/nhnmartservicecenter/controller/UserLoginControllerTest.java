package com.nhnacademy.nhnmartservicecenter.controller;

import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.exception.UserWrongLogoutException;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserLoginController.class)
class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;
    private User sampleUser = new User("123","123","123",User.auth.ADMIN);

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserLoginController(userRepository)).build();
    }

    @Test
    @Order(1)
    @DisplayName("Cookie 값이 없을 때의 Login")
    void loginWithNoCookie() throws Exception {
        // given

        // when
        mockMvc.perform(get("/cs/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"))
                .andReturn();

        // then
    }

    @Test
    @Order(2)
    @DisplayName("Cookie 값이 있을 때의 Login")
    void loginWithCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("SESSION", "sampleCookie");
        MockHttpSession sampleSession = new MockHttpSession();
        sampleSession.setAttribute("userId", sampleUser.getId());
        when(userRepository.getUser(anyString())).thenReturn(sampleUser);


        // when
        mockMvc.perform(get("/cs/login")
                        .cookie(cookie)
                        .session(sampleSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/cs"))
                .andReturn();

        // then
        verify(userRepository, timeout(1)).getUser(anyString());
    }


    @Test
    @Order(3)
    @DisplayName("정상적 Login 수행성공")
    void doRightLogin() throws Exception {
        //given
        when(userRepository.match(anyString(), anyString())).thenReturn(true);
        when(userRepository.getUser(anyString())).thenReturn(sampleUser);

        //when
        mockMvc.perform(post("/cs/login")
                        .param("id","testId")
                        .param("password","testPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/cs"))
                .andReturn();

        //then
        verify(userRepository, timeout(1)).match(anyString(), anyString());
        verify(userRepository, timeout(1)).getUser(anyString());
    }

    @Test
    @Order(4)
    @DisplayName("잘못된 id와 password 입력 후 Login 수행성공")
    void doWrongLogin() throws Exception {
        //given
        when(userRepository.match(anyString(), anyString())).thenReturn(false);

        //when
        mockMvc.perform(post("/cs/login")
                        .param("id","testId")
                        .param("password","testPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name( "redirect:/cs/login"))
                .andReturn();

        //then
        verify(userRepository, timeout(1)).match(anyString(), anyString());
    }

    @Test
    @Order(5)
    @DisplayName("정상적 logout 수행")
    void doLogoutWithSession() throws Exception {
        // given
        Cookie cookie = new Cookie("SESSION", "sampleCookie");
        MockHttpSession sampleSession = new MockHttpSession();
        sampleSession.setAttribute("userId", sampleUser.getId());
        when(userRepository.getUser(anyString())).thenReturn(sampleUser);


        // when
        mockMvc.perform(post("/cs/logout")
                        .cookie(cookie)
                        .session(sampleSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cs/login"))
                .andReturn();

        // then
    }

    @Test
    @Order(6)
    @DisplayName("쿠키,세션 없이 logout 수행")
    void doLogoutWithNoSession() throws Exception {
        // given

        // when

        // then
        Assertions.assertThrows(ServletException.class,()->
                mockMvc.perform(post("/cs/logout"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/cs/login"))
                        .andReturn()
                );
    }
}