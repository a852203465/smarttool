package cn.darkjrong.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Http 配置类
 *
 * @author Rong.Jia
 * @date 2021/01/28 16:18:22
 */
@Configuration
@ComponentScan("cn.darkjrong.httpclient")
@ConditionalOnProperty(prefix = "stl.http-client", name = "enabled", havingValue = "true")
@ConditionalOnClass({HttpClientPoolProperties.class})
public class HttpAutoConfiguration {

    @Bean
    public HttpFactoryBean httpFactoryBean() {
        return new HttpFactoryBean();
    }






















}
