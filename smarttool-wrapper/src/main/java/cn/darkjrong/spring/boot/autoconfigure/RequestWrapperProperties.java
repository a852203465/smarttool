package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 请求包装器属性
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.wrapper")
public class RequestWrapperProperties {

    /**
     *  是否开启，默认：false
     */
    private boolean enabled = false;

    /**
     * 排序
     */
    private Integer order;

    private String name = "extend-filter";

    private Map<String, String> initParameters = new LinkedHashMap<>();

    private Set<String> servletNames = new LinkedHashSet<>();

    private Set<ServletRegistrationBean<?>> servletRegistrationBeans = new LinkedHashSet<>();

    private Set<String> urlPatterns = new LinkedHashSet<>();




}
