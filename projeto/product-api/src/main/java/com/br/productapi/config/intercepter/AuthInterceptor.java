package com.br.productapi.config.intercepter;

import com.br.productapi.model.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    private  static final String AUTHORIZATION = "Authorization";
    @Autowired
    private JwtService jwtService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle)
         throws Exception {
            if (isOption(request)){
                return true;
            }
            var authorization = request.getHeader(AUTHORIZATION);
            jwtService.validateAuthorization(authorization);
            return true;
        }

    private boolean isOption(HttpServletRequest request){
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
