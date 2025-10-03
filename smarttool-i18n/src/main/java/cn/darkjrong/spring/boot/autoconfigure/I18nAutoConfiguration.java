package cn.darkjrong.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * I18n 配置
 *
 * @author Rong.Jia
 * @date 2025/03/26
 */
@Configuration
@ComponentScan("cn.darkjrong.i18n")
@EnableConfigurationProperties(I18nProperties.class)
public class I18nAutoConfiguration {





}
