package learn.springboot.sharding.jdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_order")
public class Order {

    @TableId(type = IdType.INPUT)
    private Long orderId;
    private Long userId;
    private BigDecimal money;
    private Date createTime;
}
