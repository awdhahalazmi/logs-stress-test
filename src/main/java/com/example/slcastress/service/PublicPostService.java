package com.example.slcastress.service;
import com.example.slcastress.integration.PublicPostsIntegration;
import com.example.slcastress.model.ExternalPost;
import org.springframework.stereotype.Service;
@Service public class PublicPostService {
 private final PublicPostsIntegration integration;
 public PublicPostService(PublicPostsIntegration integration){this.integration=integration;}
 public ExternalPost getPost(long id){return integration.fetchPost(id);}
 public ExternalPost publish(ExternalPost post){return integration.createPost(post);}
 public String riskyBranch(String input){if(input==null)throw new IllegalArgumentException("input missing");if(input.isBlank())return "EMPTY";try{if("explode".equals(input))throw new IllegalStateException("Simulated exception");return input.trim();}catch(IllegalStateException e){throw e;}finally{input.length();}}
}
