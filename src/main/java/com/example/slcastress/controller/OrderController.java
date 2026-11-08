package com.example.slcastress.controller;
import com.example.slcastress.dto.*;
import com.example.slcastress.model.Order;
import com.example.slcastress.service.OrderService;
import com.example.slcastress.middleware.ApiKeyGuard;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/orders") public class OrderController {
 private final OrderService service;private final ApiKeyGuard guard;
 public OrderController(OrderService service,ApiKeyGuard guard){this.service=service;this.guard=guard;}
 @PostMapping public OrderResponse create(@RequestHeader(value="X-Demo-Key",required=false) String key,@Valid @RequestBody CreateOrderRequest request){guard.validate(key);return new OrderResponse(true,"Created",service.create(request));}
 @GetMapping("/{id}") public OrderResponse get(@PathVariable long id){return new OrderResponse(true,"Found",service.get(id));}
 @GetMapping public List<Order> list(@RequestParam(required=false) String status){return service.list(status);}
 @PutMapping("/{id}/status") public OrderResponse update(@PathVariable long id,@RequestParam String status){return new OrderResponse(true,"Updated",service.changeStatus(id,status));}
 @DeleteMapping("/{id}") public void cancel(@PathVariable long id){service.cancel(id);}
}
