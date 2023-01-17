package jenny.learn.springboot.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class JasyptTest {
    @Resource
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

    @Test
    public void testEncrypt() {
        log.info("密文：{}", stringEncryptor.encrypt("abc123"));
    }
}
