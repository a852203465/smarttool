package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.retry.RetryTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 重试工厂类
 *
 * @author Rong.Jia
 * @date 2025/09/28
 */
@Slf4j
public class RetryFactoryBean implements FactoryBean<RetryTemplate>, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private RetryTemplate retryTemplate;
    private final RetryPlanProperties retryPlanProperties;

    public RetryFactoryBean(RetryPlanProperties retryPlanProperties) {
        this.retryPlanProperties = retryPlanProperties;
    }

    @Override
    public void afterPropertiesSet() {
        retryTemplate = new RetryTemplate(retryPlanProperties);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public RetryTemplate getObject() {
        return this.retryTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RetryTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
