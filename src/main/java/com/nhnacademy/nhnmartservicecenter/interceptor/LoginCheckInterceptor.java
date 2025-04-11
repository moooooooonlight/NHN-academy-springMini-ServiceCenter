package com.nhnacademy.nhnmartservicecenter.interceptor;

import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.exception.UserNotLoginException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 쿠키 확인
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(!cookie.getName().equals("SESSION")){
                throw new UserNotLoginException();
            }
        }

        // 세션값에 유저정보가 있는지 확인
        HttpSession session = request.getSession();
        if(session == null || session.getAttribute("userId")==null){
            throw new UserNotLoginException();
        }

        return true;
    }
}
