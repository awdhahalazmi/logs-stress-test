package com.example.slcastress.dto;
import com.example.slcastress.model.ExternalPost;
public record ExternalResponse(boolean success, String source, ExternalPost data) {}
