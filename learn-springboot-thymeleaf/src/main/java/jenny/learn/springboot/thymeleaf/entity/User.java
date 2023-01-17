package jenny.learn.springboot.thymeleaf.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 */
@Data
@Entity
@Table(name = "t_user")
public class User implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * 用户编号
     */
    @Column(name = "user_code")
    private String userCode;
    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
