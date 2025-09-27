package cn.darkjrong.swagger.geteway;

import springfox.documentation.swagger.web.SwaggerResource;

/**
 * Swagger 工具类
 *
 * @author Rong.Jia
 * @date 2021/07/09
 */
public class SwaggerUtils {

//    private static final String SWAGGER_VERSION = "3.0";
//    public static final String API_URI = "/v3/api-docs";

    /**
     * Swagger的资源
     *
     * @param name     的名字
     * @param location 位置
     * @param swaggerVersion swagger 版本
     * @return {@link SwaggerResource} Swagger资源
     */
    public static SwaggerResource swaggerResource(String name, String location, String swaggerVersion) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(swaggerVersion);
        return swaggerResource;
    }

}
