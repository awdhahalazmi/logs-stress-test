package com.example.slcastress.dto;
import java.time.Instant;
public record ErrorResponse(boolean success,String code,String message,Instant timestamp) {}
