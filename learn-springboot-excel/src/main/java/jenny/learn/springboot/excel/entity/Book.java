package jenny.learn.springboot.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_book")
public class Book {
    @TableId
    @ExcelIgnore
    private String id;

    @ExcelProperty(value = "书名")
    @ColumnWidth(50)
    private String name;

    @ExcelProperty("作者")
    private String author;

    /** 封面图片地址 */
    @ExcelIgnore
    private String coverUrl;

    @ExcelProperty("出版社")
    @ColumnWidth(20)
    private String publisher;

    @ExcelProperty("评分")
    private Double score;

    /** 状态 */
    @ExcelIgnore
    private Integer status;

    /** 排序序号 */
    @ExcelIgnore
    private Integer orderNo;

    /** 创建者 */
    @ExcelIgnore
    private String createBy;

    /** 创建时间 */
    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @ExcelIgnore
    private String updateBy;

    /** 更新时间 */
    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
