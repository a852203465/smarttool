package cn.darkjrong.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 重试自动配置类
 *
 * @author Rong.Jia
 * @date 2025/09/27
 */
@Configuration
@EnableConfigurationProperties(RetryPlanProperties.class)
@ComponentScan("cn.darkjrong.retry")
public class RetryAutoConfiguration {

    @Bean
    public RetryFactoryBean retryFactoryBean(RetryPlanProperties retryPlanProperties) {
        return new RetryFactoryBean(retryPlanProperties);
    }



}
