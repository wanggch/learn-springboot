package jenny.learn.springboot.jdbc.dao;

import com.google.gson.Gson;
import jenny.learn.springboot.jdbc.entity.ColumnInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ColumnDao {

    private JdbcTemplate jdbcTemplate;

    public List<ColumnInfo> columns(String tableName) {
        String sql = "SELECT * FROM `information_schema`.`COLUMNS` WHERE TABLE_SCHEMA='bladex' AND TABLE_NAME=? ORDER BY ORDINAL_POSITION";
        List<ColumnInfo> columnInfoList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ColumnInfo.class), new Object[] { tableName });
        columnInfoList.forEach(columnInfo -> log.info(new Gson().toJson(columnInfo)));
        return columnInfoList;
    }
}
