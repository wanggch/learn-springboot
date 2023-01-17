package jenny.learn.springboot.jdbc.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ColumnServiceTest {
    @Resource
    private ColumnService columnService;
    @Test
    public void test() {
        String tableName = "blade_work_order";
        columnService.template(tableName);
    }
}
