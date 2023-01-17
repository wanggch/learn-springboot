package learn.springboot.sharding.jdbc.service;

import learn.springboot.sharding.jdbc.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
public class OrderServiceTests {

    @Resource
    private OrderService orderService;

    @Test
    public void testInsert() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUserId(1L);
        order.setMoney(new BigDecimal(10));
        orderService.insert(order);
    }
}
