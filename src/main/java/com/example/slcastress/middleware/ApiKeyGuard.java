package com.example.slcastress.middleware;
import com.example.slcastress.exception.BusinessException;
import org.springframework.stereotype.Component;
@Component public class ApiKeyGuard {
 public void validate(String apiKey){if(apiKey==null||apiKey.isBlank())throw new BusinessException("Missing X-Demo-Key"); if(!"demo-key".equals(apiKey))throw new BusinessException("Invalid API key");}
 public String normalize(String value){if(value==null)return "UNKNOWN"; return value.strip().toUpperCase();}
}
