package cn.darkjrong.swagger.common.configuration;

import cn.darkjrong.swagger.common.utils.SwaggerUtils;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Swagger 摘要信息配置
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
@Configuration
@AllArgsConstructor
public class DocketInfoConfiguration implements BeanFactoryAware {

    private static final String DEFAULT_BASE_PATH = "/**";
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

    private BeanFactory beanFactory;
    private final SwaggerProperties swaggerProperties;
    private final SwaggerAuthorizationConfiguration authConfiguration;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private static final String BEAN_NAME = "swagger-spring-boot-starter-";

    @Bean
    public void createRestApi() {
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;

        Map<String, SwaggerProperties.DocketInfo> docketInfoMap = swaggerProperties.getDocket();
        if (CollectionUtil.isEmpty(docketInfoMap)) {
            String beanName = BEAN_NAME + "default";

            registerBeanDefinition(beanRegistry, beanName);
            Docket docket4Group = (Docket)beanFactory.getBean(beanName);

            ApiSelectorBuilder apis = docket4Group.host(swaggerProperties.getHost())
                    .apiInfo(apiInfo(swaggerProperties))
                    .pathMapping(swaggerProperties.getPathMapping())
                    .ignoredParameterTypes(getIgnoredParameterTypes(swaggerProperties.getIgnoredParameterTypes())).select()
                    .apis(SwaggerUtils.basePackages(swaggerProperties.getBasePackages()));


            getBasePath().forEach(p -> apis.paths(PathSelectors.ant(p)));
            getExcludePath().forEach(p -> apis.paths(PathSelectors.ant(p).negate()));

            apis.build()
                    .securityContexts(CollectionUtil.newArrayList(authConfiguration.securityContext()))
                    .securitySchemes(authConfiguration.getSecuritySchemes());
        }else {

            for (Map.Entry<String, SwaggerProperties.DocketInfo> entry : swaggerProperties.getDocket().entrySet()) {
                SwaggerProperties.DocketInfo docketInfo = entry.getValue();
                String beanName = BEAN_NAME + entry.getKey();
                registerBeanDefinition(beanRegistry, beanName);

                ApiInfo apiInfo = new ApiInfoBuilder()
                        .title(StrUtil.isBlank(docketInfo.getTitle()) ? swaggerProperties.getTitle() : docketInfo.getTitle())
                        .description(StrUtil.isBlank(docketInfo.getDescription()) ? swaggerProperties.getDescription() : docketInfo.getDescription())
                        .version(StrUtil.isBlank(docketInfo.getVersion()) ? swaggerProperties.getVersion() : docketInfo.getVersion())
                        .license(StrUtil.isBlank(docketInfo.getLicense()) ? swaggerProperties.getLicense() : docketInfo.getLicense())
                        .licenseUrl(StrUtil.isBlank(docketInfo.getLicenseUrl()) ? swaggerProperties.getLicenseUrl() : docketInfo.getLicenseUrl())
                        .contact(new Contact(
                                StrUtil.isBlank(docketInfo.getContact().getName()) ? swaggerProperties.getContact().getName() : docketInfo.getContact().getName(),
                                StrUtil.isBlank(docketInfo.getContact().getUrl()) ? swaggerProperties.getContact().getUrl() : docketInfo.getContact().getUrl(),
                                StrUtil.isBlank(docketInfo.getContact().getEmail()) ? swaggerProperties.getContact().getEmail() : docketInfo.getContact().getEmail()))
                        .termsOfServiceUrl(StrUtil.isBlank(docketInfo.getTermsOfServiceUrl()) ? swaggerProperties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                        .build();

                Docket docket4Group = (Docket)beanFactory.getBean(beanName);
                ApiSelectorBuilder apis = docket4Group.host(StrUtil.isBlank(docketInfo.getHost()) ? swaggerProperties.getHost() : docketInfo.getHost())
                        .apiInfo(apiInfo)
                        .groupName(StrUtil.isBlank(docketInfo.getGroupName()) ? entry.getKey() : docketInfo.getGroupName())
                        .pathMapping(StrUtil.isBlank(docketInfo.getPathMapping()) ? swaggerProperties.getPathMapping() : docketInfo.getPathMapping())
                        .ignoredParameterTypes(getIgnoredParameterTypes(CollectionUtil.isEmpty(docketInfo.getIgnoredParameterTypes()) ? swaggerProperties.getIgnoredParameterTypes() : docketInfo.getIgnoredParameterTypes())).select()
                        .apis(SwaggerUtils.basePackages(docketInfo.getBasePackages()));


                getBasePath().forEach(p -> apis.paths(PathSelectors.ant(p)));
                getExcludePath().forEach(p -> apis.paths(PathSelectors.ant(p).negate()));

                apis.build()
                        .securityContexts(CollectionUtil.newArrayList(authConfiguration.securityContext()))
                        .securitySchemes(authConfiguration.getSecuritySchemes()).select();
            }
        }
    }

    /**
     * 获取基本路径
     *
     * @return {@link List}<{@link String}>
     */
    private List<String> getBasePath() {
        List<String> basePath = swaggerProperties.getBasePath();
        if (CollectionUtil.isEmpty(basePath)) {
            basePath.add(DEFAULT_BASE_PATH);
        }
        return basePath;
    }

    /**
     * 获取排除路径
     *
     * @return {@link List}<{@link String}>
     */
    private List<String> getExcludePath() {
        List<String> excludePath = swaggerProperties.getExcludePath();
        if (CollectionUtil.isEmpty(excludePath)) {
            excludePath.addAll(DEFAULT_EXCLUDE_PATH);
        }
        return excludePath;
    }

    /**
     * 获取忽略参数类型
     *
     * @param ignoredParameterTypes 忽略参数类型
     * @return {@link Class}<{@link ?}>{@link []}
     */
    private Class<?>[] getIgnoredParameterTypes(List<Class<?>> ignoredParameterTypes) {
        Class<?>[] array = new Class[ignoredParameterTypes.size()];
        return ignoredParameterTypes.toArray(array);
    }

    /**
     * 注册Bean定义
     *
     * @param beanRegistry Bean注册表
     * @param beanName     Bean的名字
     */
    private void registerBeanDefinition(BeanDefinitionRegistry beanRegistry, String beanName) {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, DocumentationType.OAS_30);
        beanDefinition.setBeanClassName(Docket.class.getName());
        beanDefinition.setRole(BeanDefinition.ROLE_SUPPORT);
        beanRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 配置基本信息
     *
     * @param swaggerProperties 昂首阔步的属性
     * @return {@link ApiInfo}
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }









}
