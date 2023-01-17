package jenny.learn.springboot.mybatis.enums;

import jenny.learn.springboot.mybatis.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 交易类型
 * @author: wanggc
 */
@Getter
@AllArgsConstructor
public enum TransferType implements BaseEnum {

    /**
     * 转入
     */
    IN("1", "转入"),
    /**
     * 转出
     */
    OUT("2", "转出");

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;

    /**
     * 根据交易类型编码获取交易枚举类型
     * @param code 交易类型编码
     * @return 交易枚举类型
     */
    public static TransferType getTransferType(String code) {
        for (TransferType transferType : TransferType.values()) {
            if (Objects.equals(code, transferType.getCode())) {
                return transferType;
            }
        }
        return null;
    }
}
