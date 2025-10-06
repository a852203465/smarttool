package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashSet;
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
public class BodyCacheProperties {

    /**
     *  是否开启，默认：false
     */
    private boolean enabled = false;

    /**
     * 需要包装的URL模式。
     */
    private Set<String> urlPatterns = new LinkedHashSet<>();




}
