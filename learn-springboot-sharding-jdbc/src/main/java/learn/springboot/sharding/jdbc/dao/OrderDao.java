package learn.springboot.sharding.jdbc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import learn.springboot.sharding.jdbc.entity.Order;

import java.util.List;

public interface OrderDao extends BaseMapper<Order> {
    List<Order> findAll();
}
