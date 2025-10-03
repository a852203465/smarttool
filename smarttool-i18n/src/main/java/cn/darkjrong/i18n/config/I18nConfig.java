package cn.darkjrong.i18n.config;

import cn.darkjrong.i18n.I18nTemplate;
import cn.darkjrong.spring.boot.autoconfigure.I18nProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * i18n Web配置
 *
 * @author Rong.Jia
 * @date 2025/03/27
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

	@Autowired
	private I18nProperties i18nProperties;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor(i18nProperties);
		registry.addInterceptor(interceptor)
				.addPathPatterns(i18nProperties.getInterceptor().getPatterns());
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
		messageBundle.setBasename(i18nProperties.getBasename());
		messageBundle.setDefaultEncoding(i18nProperties.getDefaultEncoding());
		messageBundle.setConcurrentRefresh(i18nProperties.isConcurrentRefresh());
		messageBundle.setUseCodeAsDefaultMessage(i18nProperties.isUseCodeAsDefaultMessage());
		messageBundle.setCacheSeconds(i18nProperties.getCacheSeconds());
		return messageBundle;
	}

	@Bean
	public MessageSourceAccessor defaultMessageSourceAccessor(MessageSource messageSource) {
		return new MessageSourceAccessor(messageSource);
	}

	@Bean
	public I18nTemplate i18nTemplate(MessageSourceAccessor messageSourceAccessor) {
		return new I18nTemplate(messageSourceAccessor);
	}



}
