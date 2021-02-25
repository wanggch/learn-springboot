package jenny.learn.springboot.security.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String id;
    private String code;
    private String name;
    private String password;
    private Integer orderNo;
    private Date createTime;
}
