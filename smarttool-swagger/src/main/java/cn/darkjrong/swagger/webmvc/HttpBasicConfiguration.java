package cn.darkjrong.swagger.webmvc;

import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * HttpBasic配置
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
@AllArgsConstructor
@Configuration
@ConditionalOnClass({DispatcherServlet.class, WebMvcConfigurer.class})
@ConditionalOnProperty(name = "swagger.basic.enable", havingValue = "true")
@EnableConfigurationProperties(HttpBasicProperties.class)
public class HttpBasicConfiguration {

    private final HttpBasicProperties httpBasicProperties;

    @Bean
    @ConditionalOnMissingBean(SecurityBasicAuthFilter.class)
    public SecurityBasicAuthFilter securityBasicAuthFilter() {
        return new SecurityBasicAuthFilter(httpBasicProperties.isEnabled(), httpBasicProperties.getUsername(), httpBasicProperties.getPassword());
    }


}
