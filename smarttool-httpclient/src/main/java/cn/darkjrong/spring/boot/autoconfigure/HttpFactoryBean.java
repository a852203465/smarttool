package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.httpclient.utils.RestTemplateUtils;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestTemplate;

/**
 *  Http 工厂类
 * @author Rong.Jia
 * @date 2021/02/03 09:03
 */
@Slf4j
public class HttpFactoryBean implements FactoryBean<RestTemplateUtils>, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private RestTemplateUtils restTemplateUtils;

    @Override
    public void afterPropertiesSet() {
        try {
            if (ObjectUtil.isNotNull(applicationContext)) {
                this.restTemplateUtils = new RestTemplateUtils((RestTemplate)applicationContext.getBean("httpClientTemplate"));
            }
        }catch (Exception e) {
            log.error(String.format("The alarm message callback thread failed to start, 【%s】", e.getMessage()), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public RestTemplateUtils getObject() {
        return this.restTemplateUtils;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplateUtils.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
