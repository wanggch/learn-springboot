package jenny.learn.springboot.mybatis.entity;

import jenny.learn.springboot.mybatis.enums.TransferType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransferLog implements Serializable {
    private String id;
    private BigDecimal amount;
    private String transferDate;
    private TransferType transferType;
    private Date createTime;
    private String memo;

    public String getMemo() {
        return null;
    }
}
