package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.xss.XssFilter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * XSS自动配置
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Configuration
@EnableConfigurationProperties(XssProperties.class)
@ComponentScan("cn.darkjrong.xss")
@ConditionalOnProperty(prefix = "stl.xss", name = "enabled", havingValue = "true")
public class XssAutoConfiguration {

    @Bean
    public FilterRegistrationBean xssFilterRegistration(XssProperties xssProperties) {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(9999);
        registrationBean.setFilter(new XssFilter());
        registrationBean.setName("xssFilter");
        registrationBean.setUrlPatterns(xssProperties.getUrlPatterns());

        Map<String, String> initParameters = new HashMap<>();
        List<String> excludes = xssProperties.getExcludes();
        if (CollUtil.isNotEmpty(excludes)) {
            String ex = excludes.stream().filter(StrUtil::isNotBlank).collect(Collectors.joining(","));
            initParameters.put("excludes", ex);
        }
        registrationBean.setInitParameters(initParameters);
        return registrationBean;
    }











}
