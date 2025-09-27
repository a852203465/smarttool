package cn.darkjrong.swagger.geteway;

import cn.darkjrong.spring.boot.autoconfigure.CustomZuulProperties;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合其他微服务的api资源
 *
 * @author Rong.Jia
 * @date 2021/05/11 13:33
 */
@AllArgsConstructor
public class ZuulSwaggerProvider implements SwaggerResourcesProvider {

    private RouteLocator routeLocator;
    private ZuulProperties zuulProperties;
    private CustomZuulProperties customZuulProperties;

    @Override
    public List<SwaggerResource> get() {

        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //取出gateway的route
        routeLocator.getRoutes().forEach(route -> routes.add(route.getId()));

        zuulProperties.getRoutes().entrySet().stream()
                .filter(zuulRoute -> routes.contains(zuulRoute.getValue().getId()))
                .forEach(zuulRoute -> resources.add(SwaggerUtils.swaggerResource(getName(zuulRoute.getKey()),
                        zuulRoute.getValue().getPath().replace("/**", customZuulProperties.getSwagger().getSwaggerApiDocs()),
                        customZuulProperties.getSwagger().getSwaggerVersion())));
        return resources;
    }

    /**
     * 获取名字
     *
     * @param key KEY
     * @return {@link String}
     */
    private String getName(String key) {
        CustomZuulProperties.CustomZuulRoute customZuulRoute = customZuulProperties.getRoutes().get(key);
        return ObjectUtil.isNotEmpty(customZuulRoute) ? customZuulRoute.getName() : key;
    }


















}