package com.example.slcastress;
import com.example.slcastress.service.OrderServiceImpl;
import com.example.slcastress.integration.PaymentGatewayIntegration;
import com.example.slcastress.middleware.ApiKeyGuard;
import com.example.slcastress.dto.CreateOrderRequest;
import com.example.slcastress.exception.BusinessException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class OrderServiceTest {
 @Test void createsOnceAndReadsBack(){var service=new OrderServiceImpl(new PaymentGatewayIntegration(),new ApiKeyGuard());var order=service.create(new CreateOrderRequest("cust-1","book",2,new BigDecimal("3.50"),"secret-token","person@example.com"));assertEquals(new BigDecimal("7.00"),order.total());assertEquals(order,service.get(order.id()));assertEquals(1,service.list(null).size());}
 @Test void declinedPaymentDoesNotCreateOrder(){var service=new OrderServiceImpl(new PaymentGatewayIntegration(),new ApiKeyGuard());assertThrows(IllegalStateException.class,()->service.create(new CreateOrderRequest("DECLINE","book",1,BigDecimal.ONE,"secret",null)));assertTrue(service.list(null).isEmpty());}
 @Test void statusValidation(){var service=new OrderServiceImpl(new PaymentGatewayIntegration(),new ApiKeyGuard());var order=service.create(new CreateOrderRequest("c","item",1,BigDecimal.ONE,null,null));assertThrows(BusinessException.class,()->service.changeStatus(order.id(),"UNKNOWN"));}
}
