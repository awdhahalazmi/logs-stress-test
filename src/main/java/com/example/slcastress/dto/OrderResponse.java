package com.example.slcastress.dto;
import com.example.slcastress.model.Order;
public record OrderResponse(boolean success, String message, Order data) {}
