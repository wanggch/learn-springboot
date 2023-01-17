package jenny.learn.springboot.excel.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @description ID生成器
 * @author: wanggc
 * @date:  2022/6/30 10:01
 */
public class Id {
    public static String next() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        return snowflake.nextIdStr();
    }
}
