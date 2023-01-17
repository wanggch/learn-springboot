package jenny.learn.springboot.jdbc.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.meta.Column;
import jenny.learn.springboot.jdbc.dao.ColumnDao;
import jenny.learn.springboot.jdbc.entity.ColumnInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ColumnService {

    private ColumnDao columnDao;

    public void template(String tableName) {
        List<ColumnInfo> columnInfoList = columnDao.columns(tableName);
        List<String> columnNameList = columnInfoList.stream().map(ColumnInfo::getColumnName).collect(Collectors.toList());
        String mappingTemplate = "<result column=\"{}\" property=\"{}\"/>";
        List<String> columnMappingList = columnInfoList.stream().map(columnInfo -> {
            String property = StrUtil.toCamelCase(columnInfo.getColumnName());
            return StrUtil.format(mappingTemplate, columnInfo.getColumnName(), property);
        }).collect(Collectors.toList());
        log.info("########## {} ##########", tableName);
        log.info("########## column list ##########\n{}", String.join(", ", columnNameList));
        log.info("########## resumt map ##########\n{}", String.join("\n", columnMappingList));
    }
}
