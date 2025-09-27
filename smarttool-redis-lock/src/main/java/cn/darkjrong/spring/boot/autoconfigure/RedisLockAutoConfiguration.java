package cn.darkjrong.spring.boot.autoconfigure;

import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 锁自动配置类
 *
 * @author Rong.Jia
 * @date 2023/10/11
 */
@Configuration
@AllArgsConstructor
@ComponentScan("cn.darkjrong.redis.lock")
public class RedisLockAutoConfiguration {

    private final RedissonClient redissonClient;

    @Bean
    public RedisLockFactoryBean redisLockFactoryBean() {
        return new RedisLockFactoryBean(redissonClient);
    }




}
