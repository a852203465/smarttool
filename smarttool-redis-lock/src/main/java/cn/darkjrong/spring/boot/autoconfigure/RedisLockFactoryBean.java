package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.redis.lock.RedisLockTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Redis 锁工厂Bean
 *
 * @author Rong.Jia
 * @date 2023/10/11
 */
public class RedisLockFactoryBean implements FactoryBean<RedisLockTemplate>, InitializingBean {

    private RedisLockTemplate redisLockTemplate;
    private final RedissonClient redissonClient;

    public RedisLockFactoryBean(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }

    @Override
    public RedisLockTemplate getObject() {
        return this.redisLockTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RedisLockTemplate.class;
    }

    @Override
    public void afterPropertiesSet() {
        this.redisLockTemplate = new RedisLockTemplate(redissonClient);
    }
}
