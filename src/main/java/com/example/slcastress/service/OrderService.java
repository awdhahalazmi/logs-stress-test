package com.example.slcastress.service;
import com.example.slcastress.dto.CreateOrderRequest;
import com.example.slcastress.model.Order;
import java.util.List;
public interface OrderService {Order create(CreateOrderRequest request);Order get(long id);List<Order> list(String status);Order changeStatus(long id,String status);void cancel(long id);}
