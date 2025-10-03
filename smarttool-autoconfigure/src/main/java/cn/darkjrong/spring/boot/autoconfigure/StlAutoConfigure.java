package cn.darkjrong.spring.boot.autoconfigure;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * STL自动配置
 *
 * @author Rong.Jia
 * @date 2021/07/07
 */
@Configuration
@EnableSpringUtil
@ComponentScan("cn.darkjrong.autoconfigure")
@EnableConfigurationProperties(TrimStringProperties.class)
public class StlAutoConfigure {








}
