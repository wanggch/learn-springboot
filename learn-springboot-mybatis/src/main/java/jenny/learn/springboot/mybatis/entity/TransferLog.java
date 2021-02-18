package jenny.learn.springboot.mybatis.entity;

import jenny.learn.springboot.mybatis.enums.TransferType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易日志
 * @author: wanggc
 */
@Data
public class TransferLog implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 交易日期
     */
    private String transferDate;
    /**
     * 交易类型
     */
    private TransferType transferType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 说明
     */
    private String memo;
}
