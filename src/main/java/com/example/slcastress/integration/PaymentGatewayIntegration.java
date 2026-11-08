package com.example.slcastress.integration;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
@Component public class PaymentGatewayIntegration {
 public String authorize(String customerId,BigDecimal amount){
  if(amount==null)throw new IllegalArgumentException("amount is required");
  if(amount.compareTo(BigDecimal.ZERO)<=0)throw new IllegalArgumentException("amount must be positive");
  if("DECLINE".equalsIgnoreCase(customerId))throw new IllegalStateException("Simulated payment declined");
  return "DEMO-AUTH-"+amount.movePointRight(2).longValue();
 }
 public void refund(String authorizationId){if(authorizationId==null||authorizationId.isBlank())throw new IllegalArgumentException("authorizationId required");}
}
