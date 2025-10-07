package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.captcha.factory.cap.CaptchaFactory;
import cn.darkjrong.captcha.factory.store.CaptchaStore;
import cn.darkjrong.captcha.factory.store.MemoryCaptchaStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码自动配置类
 *
 * @author Rong.Jia
 * @date 2025/09/27
 */
@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
@ComponentScan("cn.darkjrong.captcha")
public class CaptchaAutoConfiguration {

    @Bean
    public CaptchaFactoryBean captchaFactoryBean(CaptchaProperties captchaProperties,
                                                 CaptchaStore captchaStore,
                                                 CaptchaFactory captchaFactory) {
        return new CaptchaFactoryBean(captchaProperties, captchaStore, captchaFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public CaptchaStore captchaStore() {
        return new MemoryCaptchaStore();
    }





}
