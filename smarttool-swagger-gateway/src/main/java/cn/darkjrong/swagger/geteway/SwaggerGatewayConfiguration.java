package cn.darkjrong.swagger.geteway;

import cn.darkjrong.spring.boot.autoconfigure.CustomGatewayProperties;
import cn.darkjrong.spring.boot.autoconfigure.CustomZuulProperties;
import com.netflix.zuul.filters.ZuulServletFilter;
import com.netflix.zuul.http.ZuulServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * swagger网关自动配置
 *
 * @author Rong.Jia
 * @date 2021/07/09
 */
@Configuration
public class SwaggerGatewayConfiguration {

    @Configuration
    @ConditionalOnClass({ZuulServlet.class, ZuulServletFilter.class})
    public class ZuulSwaggerConfiguration {

        @Bean
        @Primary
        public ZuulSwaggerProvider zuulSwaggerProvider(org.springframework.cloud.netflix.zuul.filters.RouteLocator  routeLocator,
                                                       ZuulProperties zuulProperties, CustomZuulProperties customZuulProperties) {
            return  new ZuulSwaggerProvider(routeLocator,zuulProperties, customZuulProperties);
        }

    }

    @Configuration
    @ConditionalOnClass({WebFluxConfigurer.class})
    public class GatewaySwaggerConfiguration {

        @Bean
        @Primary
        public GatewaySwaggerProvider gatewaySwaggerProvider(org.springframework.cloud.gateway.route.RouteLocator routeLocator,
                                                             GatewayProperties gatewayProperties, CustomGatewayProperties customGatewayProperties) {
            return new GatewaySwaggerProvider(routeLocator, gatewayProperties, customGatewayProperties);
        }

        @Bean
        public SwaggerHeaderFilter swaggerHeaderFilter(CustomGatewayProperties customGatewayProperties){
            return new SwaggerHeaderFilter(customGatewayProperties.getSwagger());
        }
    }





}
