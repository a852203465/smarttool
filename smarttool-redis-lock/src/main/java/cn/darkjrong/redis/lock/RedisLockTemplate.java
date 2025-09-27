package cn.darkjrong.redis.lock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Redis 锁工具类
 *
 * @author Rong.Jia
 * @date 2023/10/11
 */
@Slf4j
@AllArgsConstructor
public class RedisLockTemplate {

    private final RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 加锁操作
     *
     * @param lockName 锁名称
     * @return {@link Lock} 锁对象
     */
    public Lock lock(String lockName) {
        RLock rLock = redissonClient.getLock(lockName);
        rLock.lock();
        return rLock;
    }

    /**
     * 加锁, 过期自动释放
     *
     * @param leaseTime 自动释放锁时间, 单位：秒
     * @param lockName   锁定键
     * @return {@link RLock}
     */
    public RLock lock(String lockName, long leaseTime) {
        RLock lock = redissonClient.getLock(lockName);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 加锁操作, 过期自动释放
     *
     * @param lockName   锁名称
     * @param leaseTime 自动释放锁时间
     * @param timeUnit   时间单位
     * @return {@link Lock} 锁对象
     */
    public Lock lock(String lockName, TimeUnit timeUnit, long leaseTime) {
        RLock rLock = redissonClient.getLock(lockName);
        rLock.lock(leaseTime, timeUnit);
        return rLock;
    }

    /**
     * 尝试加锁操作
     *
     * @param lockName  锁名称
     * @param waitTime  等待时间, 单位：秒
     * @param leaseTime 自动释放锁时间, 单位：秒
     * @return boolean
     */
    public boolean tryLock(String lockName, long waitTime, long leaseTime) {
        RLock rLock = redissonClient.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(String.format("【%s】获取分布式锁, 【%s】", lockName, e.getMessage()), e);
            return false;
        }
        return getLock;
    }

    /**
     * 尝试加锁操作
     *
     * @param lockName  锁名称
     * @param waitTime  等待时间
     * @param leaseTime 自动释放锁时间
     * @param timeUnit  时间单位
     * @return boolean
     */
    public boolean tryLock(String lockName, TimeUnit timeUnit, long waitTime, long leaseTime) {
        RLock rLock = redissonClient.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            log.error(String.format("【%s】获取分布式锁, 【%s】", lockName, e.getMessage()), e);
            return false;
        }
        return getLock;
    }

    /**
     * 尝试加锁操作
     *
     * @param lockName  锁名称
     * @param waitTime  等待时间, 单位：秒
     * @return boolean
     */
    public boolean tryLock(String lockName, long waitTime) {
        RLock rLock = redissonClient.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(String.format("【%s】获取分布式锁, 【%s】", lockName, e.getMessage()), e);
            return false;
        }
        return getLock;
    }

    /**
     * 尝试加锁操作
     *
     * @param lockName  锁名称
     * @param waitTime  等待时间
     * @param timeUnit  时间单位
     * @return boolean
     */
    public boolean tryLock(String lockName, TimeUnit timeUnit, long waitTime) {
        RLock rLock = redissonClient.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitTime, timeUnit);
        } catch (InterruptedException e) {
            log.error(String.format("【%s】获取分布式锁, 【%s】", lockName, e.getMessage()), e);
            return false;
        }
        return getLock;
    }

    /**
     * 尝试加锁操作
     *
     * @param lockName  锁名称
     * @return boolean
     */
    public boolean tryLock(String lockName) {
        RLock rLock = redissonClient.getLock(lockName);
        return rLock.tryLock();
    }

    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    public void unlock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        if (lock.isHeldByCurrentThread() && lock.isLocked()) {
            lock.unlock();
        }
    }

    /**
     * 释放锁
     * @param lock 锁对象
     */
    public void unlock(Lock lock) {
        lock.unlock();
    }

    /**
     * 判断该锁是否已经被线程持有
     *
     * @param lockName 锁名称
     * @return boolean
     */
    public boolean isLock(String lockName) {
        RLock rLock = redissonClient.getLock(lockName);
        return rLock.isLocked();
    }


    /**
     * 判断该线程是否持有当前锁
     *
     * @param lockName 锁名称
     * @return boolean
     */
    public boolean isHeldByCurrentThread(String lockName) {
        RLock rLock = redissonClient.getLock(lockName);
        return rLock.isHeldByCurrentThread();
    }










}
