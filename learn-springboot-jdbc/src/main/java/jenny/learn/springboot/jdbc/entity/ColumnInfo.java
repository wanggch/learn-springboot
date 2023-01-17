package jenny.learn.springboot.jdbc.entity;

import lombok.Data;

@Data
public class ColumnInfo {
    private String columnName;
    private String dataType;
    private String columnComment;
}
