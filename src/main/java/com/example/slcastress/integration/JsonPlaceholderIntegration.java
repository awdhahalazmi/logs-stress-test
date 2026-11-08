package com.example.slcastress.integration;
import com.example.slcastress.model.ExternalPost;
import com.example.slcastress.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
@Component public class JsonPlaceholderIntegration implements PublicPostsIntegration {
 private final RestClient client; private final boolean live;
 public JsonPlaceholderIntegration(@Value("${external.api.base-url}") String base,@Value("${external.api.live:false}") boolean live){this.client=RestClient.builder().baseUrl(base).build();this.live=live;}
 @Override public ExternalPost fetchPost(long id){
  if(id<=0)throw new BusinessException("Post id must be positive");
  if(id==999)throw new IllegalStateException("Simulated upstream timeout");
  if(!live)return new ExternalPost(1L,id,"Mock public post "+id,"Offline deterministic response");
  ExternalPost post=client.get().uri("/posts/{id}",id).retrieve().body(ExternalPost.class);
  if(post==null)throw new BusinessException("Upstream returned empty post");return post;
 }
 @Override public ExternalPost createPost(ExternalPost post){
  if(post==null||post.title()==null||post.title().isBlank())throw new BusinessException("Post title required");
  if(!live)return new ExternalPost(post.userId(),101L,post.title(),post.body());
  return client.post().uri("/posts").body(post).retrieve().body(ExternalPost.class);
 }
}
