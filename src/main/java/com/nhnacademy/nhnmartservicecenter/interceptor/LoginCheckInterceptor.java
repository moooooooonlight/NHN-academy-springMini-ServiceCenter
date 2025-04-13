package com.nhnacademy.nhnmartservicecenter.interceptor;

import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.exception.UserNotLoginException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 쿠키 확인
        Cookie[] cookies = request.getCookies();
        boolean cookieCheck = false;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("SESSION")){
                cookieCheck = true;
            }
        }

        if(!cookieCheck){
            throw new UserNotLoginException();
        }

        // 세션값에 유저정보가 있는지 확인
        HttpSession session = request.getSession();
        if(session == null || session.getAttribute("userId")==null){
            throw new UserNotLoginException();
        }

        return true;
    }
}
