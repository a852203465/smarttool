package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * xss属性
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.xss")
public class XssProperties {

    /**
     * 是否开启，默认：false
     */
    private boolean enabled = true;

    /**
     * 需要排除的URL模式，这些URL不会进行XSS过滤。
     */
    private List<String> excludes;

    /**
     * 需要应用XSS过滤的URL模式。
     */
    private List<String> urlPatterns;








}
