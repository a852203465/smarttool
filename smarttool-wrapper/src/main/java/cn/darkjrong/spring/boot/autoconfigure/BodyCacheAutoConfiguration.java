package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.wrapper.BodyCacheFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Request 包装自动配置类
 *
 * @author Rong.Jia
 * @date 2025/10/06
 */
@Configuration
@EnableConfigurationProperties(BodyCacheProperties.class)
@ComponentScan("cn.darkjrong.wrapper")
@ConditionalOnProperty(prefix = "stl.wrapper", name = "enabled", havingValue = "true")
public class BodyCacheAutoConfiguration {

    @Bean
    public FilterRegistrationBean bodyCacheFilterRegistration(BodyCacheProperties bodyCacheProperties) {
        FilterRegistrationBean<BodyCacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setEnabled(bodyCacheProperties.isEnabled());
        registrationBean.setOrder(9999);
        registrationBean.setFilter(new BodyCacheFilter());
        registrationBean.setName("bodyCacheFilter");
        registrationBean.setUrlPatterns(bodyCacheProperties.getUrlPatterns());
        return registrationBean;
    }








}
