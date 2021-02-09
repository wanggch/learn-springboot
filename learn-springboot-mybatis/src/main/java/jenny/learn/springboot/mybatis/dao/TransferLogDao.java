package jenny.learn.springboot.mybatis.dao;

import jenny.learn.springboot.mybatis.entity.TransferLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferLogDao {

    void save(TransferLog transferLog);

    void saveAll(List<TransferLog> transferLogList);

    List<TransferLog> query();
}
