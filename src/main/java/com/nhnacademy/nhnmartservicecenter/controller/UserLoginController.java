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


/**
 * user의 로그인 로직을 담당하는 Controller
 */
@Slf4j
@Controller
public class UserLoginController {

    private final UserRepository userRepository;

    public UserLoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * @param sessionId  : 쿠기의 세션 Id값 읽기
     * @param request  :  request 세션 값 가져오기
     * @return  : 세션 존재 시, 로그인 폼 보여주기  / 세션 부재 시, 문의사항 리스트 화면 띄우기
     */
    @GetMapping("/cs/login")
    public String login(@CookieValue(value = "SESSION", required = false) String sessionId,
                        HttpServletRequest request){
        if(Objects.isNull(sessionId) || sessionId.isEmpty()){
            return "loginForm";
        }

        HttpSession session = request.getSession();
        return authCheck(session);
    }


    /**
     * @param id : 로그인 요청 Id
     * @param password : 로그인 요청 password
     * @param request  : userId 세션 등록
     * @param response : sessionId 쿠기 등록
     * @return  : 로그인 로직 처리 후 문의사항 리스트 화면 띄우기
     */
    @PostMapping("/cs/login")
    public String doLogin(@RequestParam String id,
                        @RequestParam String password,
                        HttpServletRequest request,
                        HttpServletResponse response){
        if(!userRepository.match(id, password)){
            return "redirect:/cs/login";
        }

        HttpSession session = request.getSession(); // true 유무의 차이는?
        session.setAttribute("userId",id);
        Cookie cookie = new Cookie("SESSION",session.getId());
        response.addCookie(cookie);

        return authCheck(session);
    }


    /**
     * @param request : 사용자 정보 세션값 제거
     * @param response : 쿠키 정보 삭제
     * @return  : 로그아웃 처리, 로그인폼 화면 띄우기
     */
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


    /**
     * @param session : 세션의 사용자 정보를 통해 user 권한 확인하기
     * @return  : 사용자 권한에 따른 문의사항 리스트 화면 띄우기
     */
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
