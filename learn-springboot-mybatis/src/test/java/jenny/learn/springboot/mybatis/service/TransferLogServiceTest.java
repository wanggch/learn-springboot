package jenny.learn.springboot.mybatis.service;

import com.google.gson.Gson;
import jenny.learn.springboot.mybatis.entity.TransferLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Slf4j
@SpringBootTest
public class TransferLogServiceTest {

    @Resource
    private TransferLogService transferLogService;

    @Test
    public void testSave() {
        BigDecimal amount = new BigDecimal(2000);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 6);
        String transferDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String transferType = "1";
        transferLogService.save(amount, transferDate, transferType);
    }

    @Test
    public void testQuery() {
        List<TransferLog> transferLogList = transferLogService.query();
        for(int i = 0; i < transferLogList.size(); i++) {
            log.info("{}: {}", i, new Gson().toJson(transferLogList.get(i)));
        }
    }
}
