package cn.darkjrong.swagger.webflux;

import cn.darkjrong.swagger.common.configuration.SwaggerProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * swagger配置
 *
 * @author Rong.Jia
 * @date 2021/12/20
 */
@Configuration
@AllArgsConstructor
@ConditionalOnClass({WebFluxConfigurer.class})
public class SwaggerWebFluxAutoConfiguration {

    private final SwaggerProperties swaggerProperties;

    @Bean
    @ConditionalOnMissingBean(WebfluxProductionSecurityFilter.class)
    public WebfluxProductionSecurityFilter webfluxProductionSecurityFilter(){
        return new WebfluxProductionSecurityFilter(swaggerProperties.isProduction());
    }

}
