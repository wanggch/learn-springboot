package jenny.learn.springboot.mybatis.util;

import jenny.learn.springboot.mybatis.base.BaseEnum;

import java.util.Objects;

/**
 * 枚举工具类
 * @author: wanggc
 */
public class BaseEnumUtil {

    public static <E extends BaseEnum> E codeOf(Class<E> enumClass, String code) {
        for (E e : enumClass.getEnumConstants()) {
            if (Objects.equals(code, e.getCode())) {
                return e;
            }
        }
        return null;
    }
}
