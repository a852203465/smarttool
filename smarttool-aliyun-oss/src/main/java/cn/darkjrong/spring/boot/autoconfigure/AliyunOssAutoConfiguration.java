package cn.darkjrong.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS自动配置
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
@Configuration
@EnableConfigurationProperties(AliyunOssProperties.class)
@ConditionalOnProperty(prefix = "stl.aliyun.oss", name = "enabled", havingValue = "true")
@ComponentScan("cn.darkjrong.aliyun.oss")
public class AliyunOssAutoConfiguration {

    private final AliyunOssProperties aliyunOssProperties;

    public AliyunOssAutoConfiguration(AliyunOssProperties aliyunOssProperties) {
        this.aliyunOssProperties = aliyunOssProperties;
    }

    @Bean
    public AliyunOssFactoryBean ossClientFactoryBean() {
        return new AliyunOssFactoryBean(aliyunOssProperties);
    }





}
