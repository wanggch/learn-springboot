package learn.springboot.sharding.jdbc.service;

import learn.springboot.sharding.jdbc.dao.OrderDao;
import learn.springboot.sharding.jdbc.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderDao orderDao;
    public int insert(Order order) {
        order.setCreateTime(new Date());
        return orderDao.insert(order);
    }
    public List<Order> all() {
        return orderDao.findAll();
    }
}
