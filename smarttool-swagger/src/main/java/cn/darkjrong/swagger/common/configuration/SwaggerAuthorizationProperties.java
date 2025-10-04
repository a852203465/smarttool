package cn.darkjrong.swagger.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger 权限认证配置
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
@Data
@ConfigurationProperties("stl.swagger.authorization")
public class SwaggerAuthorizationProperties {

    /**
     * 鉴权策略ID，对应 SecurityReferences ID
     */
    private String name = "Authorization";

    /**
     * 鉴权策略，可选 ApiKey | BasicAuth | None，默认ApiKey
     */
    private String type = "ApiKey";

    /**
     * 鉴权传递的Header参数
     */
    private String keyName = "Authorization";

    /**
     * 需要开启鉴权URL的正则
     */
    private String authRegex = "^.*$";

}


