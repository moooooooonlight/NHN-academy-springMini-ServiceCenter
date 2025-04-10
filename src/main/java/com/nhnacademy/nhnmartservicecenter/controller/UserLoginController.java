package com.nhnacademy.nhnmartservicecenter.controller;

import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Slf4j
@Controller
public class UserLoginController {

    private final UserRepository userRepository;

    public UserLoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/cs/login")
    public String login(@CookieValue(value = "SESSION", required = false) String sessionId,
                        Model model){
        if(Objects.isNull(sessionId) || sessionId.isEmpty()){
            return "loginForm";
        }else{
            return "";
        }
    }

    @PostMapping("/cs/login")
    public String doLogin(@RequestParam String id,
                        @RequestParam String password,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response){

        if(!userRepository.match(id, password)){
            return "redirect:/cs/login";
        }

        HttpSession session = request.getSession(true); // true 유무의 차이는?
        Cookie cookie = new Cookie("SESSION",session.getId());
        response.addCookie(cookie);
        session.setAttribute("id",id);

        User user = userRepository.getUser(id);
        if(user.getAuth().equals(User.auth.ADMIN)){
            return "";
        }else{
            return "";
        }
    }
}
