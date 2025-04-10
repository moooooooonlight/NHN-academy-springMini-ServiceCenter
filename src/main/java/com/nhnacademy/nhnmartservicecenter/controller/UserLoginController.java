package com.nhnacademy.nhnmartservicecenter.controller;

import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
                        HttpServletRequest request){
        if(Objects.isNull(sessionId) || sessionId.isEmpty()){
            return "loginForm";
        }

        HttpSession session = request.getSession();
        return authCheck(session);
    }

    @PostMapping("/cs/login")
    public String doLogin(@RequestParam String id,
                        @RequestParam String password,
                        HttpServletRequest request,
                        HttpServletResponse response){
        if(!userRepository.match(id, password)){
            return "redirect:/cs/login";
        }

        HttpSession session = request.getSession(); // true 유무의 차이는?
        Cookie cookie = new Cookie("SESSION",session.getId());
        response.addCookie(cookie);
        session.setAttribute("userId",id);

        log.info("id={}, password={}",id,password);
        return authCheck(session);
    }

    @PostMapping("/cs/logout")
    public String doLogout(HttpServletRequest request,
                           HttpServletResponse response){
        HttpSession session = request.getSession();
        session.removeAttribute("userId");

        Cookie cookie = new Cookie("SESSION", null);
        cookie.setPath("/cs");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/cs/login";
    }



    private String authCheck(HttpSession session) {
        String id = (String) session.getAttribute("userId");
        User user = userRepository.getUser(id);
        if(user.getAuth().equals(User.auth.ADMIN)){
            return "redirect:/admin/cs";
        }else{
            return "redirect:/custom/cs";
        }
    }
}
