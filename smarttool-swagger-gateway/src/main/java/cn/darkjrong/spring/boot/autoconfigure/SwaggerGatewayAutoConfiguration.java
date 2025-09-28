package cn.darkjrong.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * swagger网关自动配置
 *
 * @author Rong.Jia
 * @date 2021/07/09
 */
@Configuration
@EnableConfigurationProperties({CustomGatewayProperties.class, CustomZuulProperties.class})
@ComponentScan("cn.darkjrong.swagger.geteway")
public class SwaggerGatewayAutoConfiguration {







}
