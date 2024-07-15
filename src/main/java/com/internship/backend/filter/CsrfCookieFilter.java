package com.internship.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


//in order to create the CSRF token for each response that will be send to UI application, we need to create a filter class
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        //we read the CSRFToken available inside the HttpServletRequest
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        //we check if there is an header name value inside this object. If it is not null, it means that the framewotk might have generated the csrfToken
        if(Objects.nonNull(csrfToken.getHeaderName())){
            //we are populating the same HeaderName -> it's a token
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
        //the same response will be handed over to the next filter inside the filterChain
        filterChain.doFilter(request, response);
    }
}
