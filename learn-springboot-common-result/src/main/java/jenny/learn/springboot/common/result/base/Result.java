package jenny.learn.springboot.common.result.base;

import lombok.Data;

import java.io.Serializable;

/**
* @description 统一返回结果
* @author wanggc
* @date 2020/9/16 8:04 下午
*/
@Data
public class Result<T> implements Serializable {
    /**
     * 请求是否成功处理
     */
    private Boolean success;

    /**
     * 状态码
     */
    private String code;

    /**
     * 返回结果
     */
    private T data;

    /**
     * 返回消息
     */
    private String msg;

    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setData(data);
        result.setCode("200");
        return result;
    }

    public static Result fail(String code, String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
