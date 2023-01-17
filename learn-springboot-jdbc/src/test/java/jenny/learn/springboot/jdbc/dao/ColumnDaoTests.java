package jenny.learn.springboot.jdbc.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ColumnDaoTests {
    @Resource
    private ColumnDao columnDao;
    @Test
    public void test() {
        columnDao.columns("blade_fixed_asset");
    }
}
