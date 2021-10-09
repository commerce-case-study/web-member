package com.commerce.web.member.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.commerce.web.member.config.annotation.NeedLogin;
import com.commerce.web.member.config.exception.MemberUnauthorizedException;

@Component
public class NeedLoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            NeedLogin needLogin = ((HandlerMethod) handler).getMethodAnnotation(NeedLogin.class);
            if(null == needLogin) {
                throw new MemberUnauthorizedException();
            }
        }
        return true;
    }
}
