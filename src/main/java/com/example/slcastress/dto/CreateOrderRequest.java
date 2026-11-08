package com.example.slcastress.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CreateOrderRequest(@NotBlank String customerId,@NotBlank String item,@Min(1) int quantity,@NotNull @DecimalMin("0.01") BigDecimal unitPrice,String token,String email) {}
