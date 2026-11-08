package com.example.slcastress.controller;
import com.example.slcastress.service.*;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/scenarios") public class ScenarioController {
 private final FailureScenarioService failures;private final OrderServiceImpl orders;private final PublicPostService posts;
 public ScenarioController(FailureScenarioService failures,OrderServiceImpl orders,PublicPostService posts){this.failures=failures;this.orders=orders;this.posts=posts;}
 @GetMapping("/{type}") public Map<String,Object> scenario(@PathVariable String type){return Map.of("result",failures.scenario(type));}
 @GetMapping("/branch/{value}") public Map<String,Object> branch(@PathVariable int value){return Map.of("result",orders.nestedBranches(value),"sum",failures.loopAndBranch(Math.max(0,value)));}
 @GetMapping("/risky") public Map<String,Object> risky(@RequestParam String input){return Map.of("result",posts.riskyBranch(input));}
 @GetMapping("/void/{fail}") public Map<String,Object> voidScenario(@PathVariable boolean fail){failures.voidFailure(fail);return Map.of("result","OK");}
}
