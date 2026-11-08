package com.example.slcastress.service;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class FailureScenarioService {
 public String scenario(String type){
  if(type==null)throw new IllegalArgumentException("type required");
  return switch(type){case "validation" -> throw new IllegalArgumentException("Simulated validation failure");case "null" -> Objects.requireNonNull(null,"Simulated null failure").toString();case "runtime" -> throw new IllegalStateException("Simulated runtime failure");case "early" -> "EARLY_RETURN";case "success" -> "OK";default -> throw new UnsupportedOperationException("Unknown scenario");};
 }
 public int loopAndBranch(int value){int sum=0;for(int i=0;i<value;i++){if(i%2==0)continue;sum+=i;if(sum>100)break;}return sum;}
 public void voidFailure(boolean fail){if(fail)throw new IllegalStateException("Simulated void failure");}
}
