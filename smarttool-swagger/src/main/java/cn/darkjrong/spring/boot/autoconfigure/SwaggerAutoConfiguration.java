package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.swagger.common.configuration.DocketPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.oas.configuration.OpenApiDocumentationConfiguration;

/**
 * swagger配置
 *
 * @author Rong.Jia
 * @date 2021/12/20
 */
@Configuration
@ComponentScan({
        "com.github.xiaoymin.knife4j.spring",
        "cn.darkjrong.swagger",
})
@Import({BeanValidatorPluginsConfiguration.class,
        OpenApiDocumentationConfiguration.class})
public class SwaggerAutoConfiguration {

    @Bean
    public DocketPostProcessor docketPostProcessor() {
        return new DocketPostProcessor();
    }







}
