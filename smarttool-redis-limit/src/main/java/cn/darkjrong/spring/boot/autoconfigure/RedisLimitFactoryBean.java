package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.redis.RedisUtils;
import cn.darkjrong.redis.limit.RedisLimitTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Redis限流 工厂类
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Slf4j
public class RedisLimitFactoryBean implements FactoryBean<RedisLimitTemplate>, InitializingBean {

    private RedisLimitTemplate redisLimitTemplate;
    private final RedisUtils redisUtils;

    public RedisLimitFactoryBean(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void afterPropertiesSet() {
        redisLimitTemplate = new RedisLimitTemplate(redisUtils);
    }

    @Override
    public RedisLimitTemplate getObject() {
        return this.redisLimitTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RedisLimitTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
