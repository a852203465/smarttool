package cn.darkjrong.swagger.common.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * swagger docket 处理器
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
public class DocketPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) beanFactory;
        String beanName = "documentationPluginRegistry";
        if (beanRegistry.containsBeanDefinition(beanName)) {
            BeanDefinition documentationPluginRegistryBeanDefinition = beanRegistry.getBeanDefinition(beanName);
            documentationPluginRegistryBeanDefinition.setDependsOn("createRestApi");
        }
    }
}