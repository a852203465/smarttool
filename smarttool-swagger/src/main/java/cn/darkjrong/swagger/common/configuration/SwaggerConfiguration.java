package cn.darkjrong.swagger.common.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
@Configuration
@AllArgsConstructor
public class SwaggerConfiguration {

    private final SwaggerProperties swaggerProperties;

    @Bean
    @ConditionalOnMissingBean(OpenApiExtensionResolver.class)
    public OpenApiExtensionResolver markdownResolver(){
        OpenApiExtendSetting setting = swaggerProperties.getSetting();
        if (setting==null){
            setting=new OpenApiExtendSetting();
        }

        return new OpenApiExtensionResolver(setting, CollectionUtil.newArrayList());
    }


}
