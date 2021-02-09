package jenny.learn.springboot.mybatis.enums;

import jenny.learn.springboot.mybatis.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum TransferType implements BaseEnum {

    IN("1", "转入"), OUT("2", "转出");

    private String code;
    private String name;

    public static TransferType getTransferType(String type) {
        for (TransferType transferType : TransferType.values()) {
            if (Objects.equals(type, transferType.getCode())) {
                return transferType;
            }
        }
        return null;
    }
}
