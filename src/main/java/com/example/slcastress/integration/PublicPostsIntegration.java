package com.example.slcastress.integration;
import com.example.slcastress.model.ExternalPost;
public interface PublicPostsIntegration {ExternalPost fetchPost(long id); ExternalPost createPost(ExternalPost post);}
