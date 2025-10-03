package cn.darkjrong.swagger.webmvc;

import cn.darkjrong.swagger.common.configuration.SwaggerProperties;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Swagger 配置
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
@Configuration
@AllArgsConstructor
@ConditionalOnClass({DispatcherServlet.class, WebMvcConfigurer.class})
public class SwaggerWebMvcConfiguration {

    private final SwaggerProperties swaggerProperties;

    @Bean
    @ConditionalOnMissingBean(ProductionSecurityFilter.class)
    public ProductionSecurityFilter productionSecurityFilter(){
        return new ProductionSecurityFilter(swaggerProperties.isProduction());
    }


}
