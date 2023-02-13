package com.br.productapi.config.intercepter;

import com.br.productapi.config.exeception.ValidationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class FeignClientAuthIntercepter implements RequestInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    @Override
    public void apply(RequestTemplate template){
        var currentRequest = getCurrentRequest();
        template.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION));
    }
    private HttpServletRequest getCurrentRequest(){
        try{
            //Convertemos o RequestContextHolder do Spring, pegamos o atributo do objeto ServletRequestAttributes e demos um getRequest
            return ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

        }catch (Exception ex){
            ex.printStackTrace();
                throw new ValidationException("The current request could not be processed");

        }
    }
}
