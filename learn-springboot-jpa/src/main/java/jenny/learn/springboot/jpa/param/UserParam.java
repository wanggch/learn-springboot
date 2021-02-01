package jenny.learn.springboot.jpa.param;

import jenny.learn.springboot.jpa.annotation.Query;
import lombok.Data;

@Data
public class UserParam {

    @Query
    private Integer id;

    @Query(type = Query.Type.INNER_LIKE)
    private String userCode;

    @Query(type = Query.Type.INNER_LIKE)
    private String userName;
}
