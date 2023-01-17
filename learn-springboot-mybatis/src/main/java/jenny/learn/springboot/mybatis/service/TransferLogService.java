package jenny.learn.springboot.mybatis.service;

import jenny.learn.springboot.mybatis.dao.TransferLogDao;
import jenny.learn.springboot.mybatis.entity.TransferLog;
import jenny.learn.springboot.mybatis.enums.TransferType;
import jenny.learn.springboot.mybatis.util.Id;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransferLogService {

    @Resource
    private TransferLogDao transferLogDao;

    public void save(BigDecimal amount, String transferDate, String transferType) {
        TransferLog transferLog = new TransferLog();
        transferLog.setId(Id.next());
        transferLog.setAmount(amount);
        transferLog.setTransferDate(transferDate);
        transferLog.setTransferType(TransferType.getTransferType(transferType));
        transferLog.setCreateTime(new Date());
        transferLogDao.save(transferLog);
    }

    public List<TransferLog> query() {
        return transferLogDao.query();
    }

    public TransferLog findById(String id) {
        return transferLogDao.findById(id);
    }
}
