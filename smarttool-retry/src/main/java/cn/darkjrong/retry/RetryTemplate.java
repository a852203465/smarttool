package cn.darkjrong.retry;

import cn.darkjrong.retry.handler.ConsumerHandler;
import cn.darkjrong.retry.handler.FunctionHandler;
import cn.darkjrong.retry.handler.FunctionHandlerThrow;
import cn.darkjrong.spring.boot.autoconfigure.RetryPlanProperties;

import java.util.List;

/**
 * 重试工具类
 *
 * @author Rong.Jia
 * @date 2025/09/28
 */
public class RetryTemplate {

    private final RetryPlanProperties retryPlanProperties;

    public RetryTemplate(RetryPlanProperties retryPlanProperties) {
        this.retryPlanProperties = retryPlanProperties;
    }

    /**
     * 函数式接口FunctionHandler.apply调用
     *
     * @param handler 处理器
     * @return {@link T }
     */
    public <T> T apply(FunctionHandler<T> handler) {
        int i = 1;
        while (true) {
            try {
                return handler.apply();
            } catch (Exception e) {
                if (++i > retryPlanProperties.getMaxAttempts()) {
                    throw e;
                }
            }
        }
    }

    /**
     * 函数式接口FunctionHandler.apply实现
     *
     * @param handler 处理器
     * @return {@link T }
     * @throws Exception 异常
     */
    public <T> T applyThrow(FunctionHandlerThrow<T> handler) throws Exception {
        int i = 1;
        while (true) {
            try {
                return handler.apply();
            } catch (Exception e) {
                if (++i > retryPlanProperties.getMaxAttempts()) {
                    throw e;
                }
            }
        }
    }

    /**
     * 函数式接口ConsumerHandler.accept调用
     *
     * @param handler 处理器
     */
    public void accept(ConsumerHandler handler) {
        int i = 1;
        while (true) {
            try {
                handler.accept();
                return;
            } catch (Exception e) {
                if (++i > retryPlanProperties.getMaxAttempts()) {
                    throw e;
                }
            }
        }
    }

    /**
     * 有输入有输出场景下的默认重试计划
     *
     * @param handler 处理器
     * @return {@link T }
     */
    public <T> T scheduleApply(FunctionHandler<T> handler) {
        return scheduleApply(handler, retryPlanProperties.getPlans());
    }

    /**
     * 有输入无输出场景下的重试计划
     *
     * @param handler 处理器
     */
    public void scheduleAccept(ConsumerHandler handler) {
        scheduleAccept(handler, retryPlanProperties.getPlans());
    }

    /**
     * 自定义重试计划（有输入有输出场景使用）
     *
     * @param handler           处理器
     * @param schedulingConfigs 调度配置
     * @return {@link T }
     */
    public <T> T scheduleApply(FunctionHandler<T> handler, List<RetryPlanProperties.ScheduleProperties> schedulingConfigs) {
        int i = 1;
        int index = 0;
        RetryPlanProperties.ScheduleProperties scheduleConfig = schedulingConfigs.get(index);
        while (true) {
            try {
                return handler.apply();
            } catch (Exception e) {
                if (i++ >= scheduleConfig.getRetryTimes()) {
                    i = 1;
                    scheduleConfig = schedulingConfigs.get(++index);
                }
                scheduleConfig.sleep();
            }
        }
    }

    /**
     * 自定义重试计划（有输入无输出的场景）
     *
     * @param handler           处理器
     * @param schedulingConfigs 调度配置
     */
    public void scheduleAccept(ConsumerHandler handler, List<RetryPlanProperties.ScheduleProperties> schedulingConfigs) {
        int i = 1;
        int index = 0;
        RetryPlanProperties.ScheduleProperties scheduleConfig = schedulingConfigs.get(index);
        while (true) {
            try {
                handler.accept();
                return;
            } catch (Exception e) {
                if (i++ >= scheduleConfig.getRetryTimes()) {
                    i = 1;
                    scheduleConfig = schedulingConfigs.get(++index);
                }
                scheduleConfig.sleep();
            }
        }
    }

}

