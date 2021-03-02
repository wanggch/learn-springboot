package jenny.learn.springboot.security.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户
 * @author: wanggc
 */
@Data
public class User {
    /**
     * 主键
     */
    private String id;
    /**
     * 用户编码
     */
    private String code;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 显示序号
     */
    private Integer orderNo;
    /**
     * 创建时间
     */
    private Date createTime;
}
