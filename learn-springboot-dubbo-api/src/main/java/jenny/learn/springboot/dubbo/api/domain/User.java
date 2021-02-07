package jenny.learn.springboot.dubbo.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private String id;
    private String code;
    private String name;
    private Date createTime;
}
