package cn.darkjrong.swagger.webmvc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HttpBasic 属性
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
@Data
@ConfigurationProperties(prefix = "stl.swagger.basic")
public class HttpBasicProperties {

    /**
     * basic 是否开启,默认为false
     */
    private boolean enabled = false;

    /**
     * basic 用户名
     */
    private String username;

    /**
     * basic 密码
     */
    private String password;




}
