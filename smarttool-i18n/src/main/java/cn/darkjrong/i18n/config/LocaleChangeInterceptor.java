package cn.darkjrong.i18n.config;

import cn.darkjrong.spring.boot.autoconfigure.I18nProperties;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 本地化信息拦截器，用于加载本地化信息
 *
 * @author Rong.Jia
 * @date 2025/03/26
 */
@Slf4j
public class LocaleChangeInterceptor implements AsyncHandlerInterceptor {

	private final I18nProperties i18nProperties;

    public LocaleChangeInterceptor(I18nProperties i18nProperties) {
        this.i18nProperties = i18nProperties;
    }

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		String newLocale = resolveLocal(request);
		if (StrUtil.isNotBlank(newLocale)) {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (ObjectUtil.isNull(localeResolver)) {
				throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
			}
			try {
				localeResolver.setLocale(request, response, this.parseLocaleValue(newLocale));
			} catch (IllegalArgumentException e) {
				log.error(String.format("Ignoring invalid locale value 【%s】, 【%s】", newLocale, e.getMessage()) , e);
			}
		}
		return true;
	}
	
	private Locale parseLocaleValue(String locale) {
		return Locale.forLanguageTag(locale);
	}
	
	private String resolveLocal(HttpServletRequest request) {
		I18nProperties.Interceptor interceptor = i18nProperties.getInterceptor();
		String locale = request.getHeader(interceptor.getName());
		if (StrUtil.isBlank(locale)) {
			locale = request.getParameter(interceptor.getName());
		}
		return locale;
	}
	
}
