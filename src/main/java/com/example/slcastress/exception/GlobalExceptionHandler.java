package com.example.slcastress.exception;
import com.example.slcastress.dto.ErrorResponse;
import java.time.Instant;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
@RestControllerAdvice public class GlobalExceptionHandler {
 @ExceptionHandler(BusinessException.class) public ResponseEntity<ErrorResponse> business(BusinessException e){return ResponseEntity.badRequest().body(new ErrorResponse(false,"BUSINESS_VALIDATION",e.getMessage(),Instant.now()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(new ErrorResponse(false,"INVALID_REQUEST","Request validation failed",Instant.now()));}
 @ExceptionHandler(Exception.class) public ResponseEntity<ErrorResponse> unexpected(Exception e){return ResponseEntity.status(500).body(new ErrorResponse(false,"INTERNAL_ERROR","Unexpected server error",Instant.now()));}
}
