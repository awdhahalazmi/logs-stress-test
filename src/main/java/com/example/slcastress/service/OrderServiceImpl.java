package com.example.slcastress.service;
import com.example.slcastress.dto.CreateOrderRequest;
import com.example.slcastress.model.Order;
import com.example.slcastress.exception.BusinessException;
import com.example.slcastress.integration.PaymentGatewayIntegration;
import com.example.slcastress.middleware.ApiKeyGuard;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;
@Service public class OrderServiceImpl implements OrderService {
 private final ConcurrentMap<Long,Order> orders=new ConcurrentHashMap<>();private final AtomicLong sequence=new AtomicLong(100);
 private final PaymentGatewayIntegration payments;private final ApiKeyGuard guard;
 public OrderServiceImpl(PaymentGatewayIntegration payments,ApiKeyGuard guard){this.payments=payments;this.guard=guard;}
 @Override public Order create(CreateOrderRequest request){
  if(request==null)throw new BusinessException("Request required");
  if(request.quantity()<=0)throw new BusinessException("Quantity must be positive");
  var total=request.unitPrice().multiply(java.math.BigDecimal.valueOf(request.quantity()));
  payments.authorize(request.customerId(),total);
  long id=sequence.incrementAndGet();
  Order order=new Order(id,request.customerId(),guard.normalize(request.item()),request.quantity(),total,"CREATED");
  orders.put(id,order);return order;
 }
 @Override public Order get(long id){if(id<0)throw new BusinessException("Negative id");return Optional.ofNullable(orders.get(id)).orElseThrow(()->new BusinessException("Order not found"));}
 @Override public List<Order> list(String status){
  if(status==null||status.isBlank())return List.copyOf(orders.values());
  List<Order> result=new ArrayList<>();for(Order o:orders.values()){if(o.status().equalsIgnoreCase(status))result.add(o);}return result;
 }
 @Override public Order changeStatus(long id,String status){
  Order current=get(id);String normalized=guard.normalize(status);
  switch(normalized){case "CREATED","PAID","CANCELLED" -> {} default -> throw new BusinessException("Unsupported status: "+normalized);}
  if("CANCELLED".equals(current.status()))throw new BusinessException("Cancelled order is immutable");
  Order changed=new Order(current.id(),current.customerId(),current.item(),current.quantity(),current.total(),normalized);
  orders.put(id,changed);return changed;
 }
 @Override public void cancel(long id){Order current=get(id);if("CANCELLED".equals(current.status()))return;changeStatus(id,"CANCELLED");}
 public String nestedBranches(int n){if(n<0)throw new IllegalArgumentException("negative");if(n==0)return "ZERO";if(n<10){for(int i=0;i<n;i++){if(i==7)return "SEVEN";}return "SMALL";}try{if(n==13)throw new IllegalStateException("unlucky");return "LARGE";}catch(IllegalStateException e){return "RECOVERED";}finally{Math.abs(n);}}
}
