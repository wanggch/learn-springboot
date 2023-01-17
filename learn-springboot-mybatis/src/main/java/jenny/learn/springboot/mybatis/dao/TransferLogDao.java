package jenny.learn.springboot.mybatis.dao;

import jenny.learn.springboot.mybatis.entity.TransferLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferLogDao {

    /**
     * 新增交易日志
     * @param transferLog 交易日志
     */
    void save(TransferLog transferLog);

    /**
     * 批量新增交易日志
     * @param transferLogList 交易日志列表
     */
    void saveAll(List<TransferLog> transferLogList);

    /**
     * 获取所有交易日志，按交易日期倒序排序
     * @return 交易日志列表
     */
    List<TransferLog> query();

    /**
     * 根据ID获取交易日志信息
     * @param id 交易日志ID
     * @return 交易日志信息
     */
    TransferLog findById(String id);
}
