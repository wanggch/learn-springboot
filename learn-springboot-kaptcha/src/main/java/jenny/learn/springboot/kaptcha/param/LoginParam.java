package jenny.learn.springboot.kaptcha.param;

import lombok.Data;

@Data
public class LoginParam {
    /**
     * 验证码缓存Key
     */
    private String uuid;
    /**
     * 验证码
     */
    private String kaptcha;

    // 其它参数
}
