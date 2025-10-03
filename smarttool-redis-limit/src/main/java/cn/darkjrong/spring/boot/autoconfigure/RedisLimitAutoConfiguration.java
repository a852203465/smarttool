package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.redis.RedisUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * redis限流自动配置
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Configuration
@ComponentScan("cn.darkjrong.redis.limit")
@EnableConfigurationProperties(CacheProperties.class)
public class RedisLimitAutoConfiguration {

    @Bean
    public RedisLimitFactoryBean redisLimitFactoryBean(RedisUtils redisUtils) {
        return new RedisLimitFactoryBean(redisUtils);
    }






}
