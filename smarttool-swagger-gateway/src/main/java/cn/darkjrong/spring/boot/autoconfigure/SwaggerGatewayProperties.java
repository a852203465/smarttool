package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;

/**
 *
 * Swagger 网关聚合属性
 * @author Rong.Jia
 * @date 2021/07/12
 */
@Data
public class SwaggerGatewayProperties {

    /**
     * swagger interface version default:  3.0
     */
    private String swaggerVersion = "3.0";

    /**
     * swagger api-docs default: /v3/api-docs
     */
    private String swaggerApiDocs = "/v3/api-docs";




}
