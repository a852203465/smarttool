package cn.darkjrong.autoconfigure;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务执行器配置
 * @author Rong.Jia
 * @date 2023/07/01
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.task.executor")
@ConditionalOnProperty(prefix = "stl.task.executor", name = "enabled", havingValue = "true")
public class TaskExecutorConfig {

    /**
     *  是否开启，默认：false
     */
    private boolean enabled = false;

    /**
     * 核心数
     */
    private int corePoolSize = 5;

    /**
     * 最大数
     */
    private int maxPoolSize = 50;

    /**
     * 活跃时间，单位：秒
     */
    private int keepAliveSeconds = 5;

    /**
     * 队列大小
     */
    private int queueCapacity = 500;

    @Primary
    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        //核心线程池大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //队列容量
        executor.setQueueCapacity(queueCapacity);
        //活跃时间
        executor.setKeepAliveSeconds(keepAliveSeconds);

        //线程名字前缀
        executor.setThreadNamePrefix("taskExecutor-");

        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


}
