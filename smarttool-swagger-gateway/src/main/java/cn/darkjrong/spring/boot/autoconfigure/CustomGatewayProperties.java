package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关属性配置
 *
 * @author Rong.Jia
 * @date 2022/10/07
 */
@Data
@Configuration
@ConditionalOnClass({WebFluxConfigurer.class})
@ConfigurationProperties("spring.cloud.gateway")
public class CustomGatewayProperties {

    /**
     * swagger
     */
    private SwaggerProperties swagger = new SwaggerProperties();

    /**
     * 路由
     */
    private List<CustomRouteDefinition> routes = new ArrayList<>();

    /**
     * 自定义路由定义属性
     *
     * @author Rong.Jia
     * @date 2022/10/07
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CustomRouteDefinition extends RouteDefinition {

        /**
         * 路由名
         */
        private String name;

    }

    /**
     * Swagger属性
     *
     * @author Rong.Jia
     * @date 2022/10/07
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SwaggerProperties extends SwaggerGatewayProperties {

        /**
         *  header Name， 默认： X-Forwarded-Prefix
         */
        private String headerName = "X-Forwarded-Prefix";



    }

}
