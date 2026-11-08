package com.example.slcastress.controller;
import com.example.slcastress.dto.ExternalResponse;
import com.example.slcastress.model.ExternalPost;
import com.example.slcastress.service.PublicPostService;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/external") public class IntegrationController {
 private final PublicPostService service;
 public IntegrationController(PublicPostService service){this.service=service;}
 @GetMapping("/posts/{id}") public ExternalResponse get(@PathVariable long id){return new ExternalResponse(true,"JSONPlaceholder or mock",service.getPost(id));}
 @PostMapping("/posts") public ExternalResponse create(@RequestBody ExternalPost post){return new ExternalResponse(true,"JSONPlaceholder or mock",service.publish(post));}
}
