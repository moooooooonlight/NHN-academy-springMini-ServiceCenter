package com.nhnacademy.nhnmartservicecenter.interceptor;

import com.nhnacademy.nhnmartservicecenter.domain.User;
import com.nhnacademy.nhnmartservicecenter.exception.UserAuthImpossibleAccessException;
import com.nhnacademy.nhnmartservicecenter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthCheckInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    public AuthCheckInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user = userRepository.getUser(userId);

        String path = request.getRequestURI();
        if(path.contains("admin")){
            if(!user.getAuth().equals(User.auth.ADMIN)){
                throw new UserAuthImpossibleAccessException();
            }
        }else{
            if(!user.getAuth().equals(User.auth.CUSTOM)){
                throw new UserAuthImpossibleAccessException();
            }
        }

        return true;
    }
}
