package com.example.slcastress.model;
import java.math.BigDecimal;
public record Order(long id, String customerId, String item, int quantity, BigDecimal total, String status) {}
