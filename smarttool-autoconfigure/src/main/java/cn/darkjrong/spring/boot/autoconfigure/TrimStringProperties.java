package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 修剪字符串属性
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.trim")
public class TrimStringProperties {

    /**
     *  是否开启，默认：false
     */
    private boolean enabled = false;





}
