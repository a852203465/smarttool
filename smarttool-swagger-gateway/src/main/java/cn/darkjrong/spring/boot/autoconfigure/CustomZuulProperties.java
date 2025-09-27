package cn.darkjrong.spring.boot.autoconfigure;

import com.netflix.zuul.filters.ZuulServletFilter;
import com.netflix.zuul.http.ZuulServlet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 网关属性配置
 *
 * @author Rong.Jia
 * @date 2022/10/07
 */
@Data
@Component
@ConditionalOnClass({ZuulServlet.class, ZuulServletFilter.class})
@ConfigurationProperties("spring.cloud.zuul")
public class CustomZuulProperties {

    /**
     * swagger
     */
    private SwaggerProperties swagger = new SwaggerProperties();

    /**
     * 路由
     */
    private Map<String, CustomZuulRoute> routes = new LinkedHashMap<>();

    /**
     * 自定义路由定义属性
     *
     * @author Rong.Jia
     * @date 2022/10/07
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CustomZuulRoute extends ZuulProperties.ZuulRoute {

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


    }


}
