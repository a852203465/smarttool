package cn.darkjrong.spring.boot.autoconfigure;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * i18n 配置信息
 *
 * @author Rong.Jia
 * @date 2025/03/26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.i18n")
public class I18nProperties {

	/**
	 * 多语言消息文件夹
	 */
	private String basename;

	/**
	 * 字符集,默认：UTF-8
	 */
	private String defaultEncoding = "UTF-8";

	/**
	 * 并发刷新,默认:true
	 */
	private boolean concurrentRefresh = true;

	/**
	 * 使用代码作为默认消息,默认:true
	 */
	private boolean useCodeAsDefaultMessage = true;

	/**
	 * 缓存时间,单位:秒,默认:5
	 */
	private Integer cacheSeconds = 5;

	/**
	 * 默认语言
	 */
	private Locale defaultLocale;

	/**
	 * 支持的语言,默认:Locale.getAvailableLocales()
	 */
	private List<Locale> supportedLocales;

	/**
	 * 拦截器
	 */
	private Interceptor interceptor;
	
	@Data
	public static class Interceptor {

		/**
		 * 多语言的请求头名,字段名
		 */
		private String name;

		/**
		 * 过滤范围
		 */
		private List<String> patterns;

	}

	public List<Locale> getSupportedLocales() {
		return CollectionUtil.isEmpty(supportedLocales)
				? Arrays.stream(Locale.getAvailableLocales()).collect(Collectors.toList())
				: supportedLocales;
	}
}
