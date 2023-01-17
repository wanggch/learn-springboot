package jenny.learn.springboot.security.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
* @description 安全配置，数据库连接密码
* @author wanggc
* @date 2020/11/6 2:36 下午
*/
@Configuration
public class SecurityConfig {

    /**
     * Jasypt string encryptor to encrypt/decrypt properties
     * @return String encryptor bean
     */
    @Bean
    @ConditionalOnMissingBean
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = encryptorConfig();
        config.setPassword("MyHoney");
        encryptor.setConfig(config);
        return encryptor;
    }

    /**
     * Jasypt string encriptor configuration
     * @return Encryptor configuration
     */
    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public SimpleStringPBEConfig encryptorConfig() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }
}
