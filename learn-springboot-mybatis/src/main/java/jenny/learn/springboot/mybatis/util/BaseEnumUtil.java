package jenny.learn.springboot.mybatis.util;

import jenny.learn.springboot.mybatis.base.BaseEnum;

import java.util.Objects;

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
