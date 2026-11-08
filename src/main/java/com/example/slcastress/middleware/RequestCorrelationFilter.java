package com.example.slcastress.middleware;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component public class RequestCorrelationFilter extends OncePerRequestFilter {
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
  String supplied=request.getHeader("X-Correlation-ID");
  String correlation=(supplied!=null && supplied.matches("[a-zA-Z0-9_-]{1,64}"))?supplied:UUID.randomUUID().toString();
  MDC.put("CorrelationId",correlation); response.setHeader("X-Correlation-ID",correlation);
  try {chain.doFilter(request,response);} finally {MDC.remove("CorrelationId");}
 }
}
