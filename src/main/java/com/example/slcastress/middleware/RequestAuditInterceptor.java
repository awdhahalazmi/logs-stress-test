package com.example.slcastress.middleware;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component public class RequestAuditInterceptor implements HandlerInterceptor {
 @Override public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler){
  if("true".equals(request.getHeader("X-Block-Request"))){response.setStatus(429);return false;}return true;
 }
 @Override public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex){int status=response.getStatus(); if(status>=500){response.setHeader("X-Demo-Failure","true");}}
}
