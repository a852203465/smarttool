package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 重试计划属性
 *
 * @author Rong.Jia
 * @date 2025/09/27
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties("stl.retry")
public class RetryPlanProperties {

    /**
     * 最大重试次数,默认：3
     */
    private Integer maxAttempts;

    /**
     * 重试计划
     */
    private List<ScheduleProperties> plans;










    @Data
    public static class ScheduleProperties {

        /**
         * 重试间隔时间
         */
        private Integer time;

        /**
         * 重试间隔时间单位
         */
        private TimeUnit timeUnit;

        /**
         * 重试次数
         */
        private Integer retryTimes;

        public void sleep() {
            try {
                timeUnit.sleep(time);
            } catch (InterruptedException e) {
                log.error(String.format("retry sleep interrupted,【%s】", e.getMessage()), e);
            }
        }
    }


}
