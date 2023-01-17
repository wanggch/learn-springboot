package jenny.learn.springboot.mybatis.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * ID生成器
 * @author: wanggc
 */
public class Id {

    public static String next() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        return snowflake.nextIdStr();
    }

}
