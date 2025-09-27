package cn.darkjrong.swagger.geteway;

import cn.darkjrong.spring.boot.autoconfigure.CustomGatewayProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * SwaggerHeaderFilter过滤器
 *
 * @author Rong.Jia
 * @date 2021/07/09
 */
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {

    private final CustomGatewayProperties.SwaggerProperties swaggerProperties;

    public SwaggerHeaderFilter(CustomGatewayProperties.SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path, swaggerProperties.getSwaggerApiDocs())) {
                return chain.filter(exchange);
            }
            String basePath = path.substring(0, path.lastIndexOf(swaggerProperties.getSwaggerApiDocs()));
            String[] basePathArr = {basePath};
            ServerHttpRequest newRequest = request.mutate().header(swaggerProperties.getHeaderName(),
                    basePathArr).build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };
    }
}