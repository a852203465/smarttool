package cn.darkjrong.i18n.config;

import cn.darkjrong.spring.boot.autoconfigure.I18nProperties;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleContextResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * 的实现
 * 使用HTTP请求中包含的请求头“lang”指定的主语言环境即，由客户端浏览器发送的语言环境，由客户端主动选择).
 * <p>注意：不支持setLocale，因为只能通过更改客户端的区域设置来更改lang头
 * {@link LocaleContextResolver}
 * @author Rong.Jia
 * @date 2025/03/26
 */
@Slf4j
@Component("localeResolver")
public class LangHeaderLocaleResolver implements LocaleContextResolver {

	private final List<Locale> supportedLocales = new ArrayList<>();
	private final String headerName;
	private final TimeZone defaultTimeZone;
	private final Locale defaultLocale;

    public LangHeaderLocaleResolver(I18nProperties i18nProperties) {
		this.headerName = i18nProperties.getInterceptor().getName();
		this.defaultTimeZone = TimeZone.getDefault();
		this.defaultLocale = i18nProperties.getDefaultLocale();
		setSupportedLocales(i18nProperties.getSupportedLocales());
    }

    public void setSupportedLocales(List<Locale> locales) {
		this.supportedLocales.clear();
		this.supportedLocales.addAll(locales);
	}

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String requestLang = request.getHeader(headerName);
		if (StrUtil.isBlank(requestLang)) {
			requestLang = request.getParameter(headerName);
		}
		Locale locale = StrUtil.isNotBlank(requestLang) ? Locale.forLanguageTag(requestLang) : defaultLocale;
		return isSupportedLocale(locale) ? locale : defaultLocale;
	}
	
	private boolean isSupportedLocale(Locale locale) {
		return (supportedLocales.isEmpty() || supportedLocales.contains(locale));
	}
	
	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		setLocaleContext(request, response, (locale != null ? new SimpleLocaleContext(locale) : null));
	}
	
	@Override
	public LocaleContext resolveLocaleContext(HttpServletRequest request) {
		return new TimeZoneAwareLocaleContext() {
			@Override
			public Locale getLocale() {
				return resolveLocale(request);
			}
			
			@Override
			public TimeZone getTimeZone() {
				return defaultTimeZone;
			}
		};
	}
	
	@Override
	public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
		LocaleContextHolder.setLocaleContext(localeContext == null ? new SimpleLocaleContext(resolveLocale(request)) : localeContext, false);
	}

}
