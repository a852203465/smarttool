package cn.darkjrong.autoconfigure;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 *  定时器连接池
 * @date 2019/6/11 20:45:22
 * @author Rong.Jia
 */
@Slf4j
@Data
@Configuration
@ConditionalOnProperty(prefix = "stl.scheduled", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "stl.scheduled")
public class ScheduledConfig {

    /**
     * 是否开启，默认：false
     */
    private boolean enabled = false;

    /**
     * 线程池个数，默认：10
     */
    private int poolSize = 10;

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize);
        taskScheduler.initialize();
        return taskScheduler;
    }

}
