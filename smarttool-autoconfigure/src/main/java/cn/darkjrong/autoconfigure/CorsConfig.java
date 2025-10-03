package cn.darkjrong.autoconfigure;

import cn.hutool.core.comparator.VersionComparator;
import lombok.Data;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 跨域配置
 *
 * @author Rong.Jia
 * @date 2021/12/20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.cors")
@ConditionalOnProperty(prefix = "stl.cors", name = "enabled", havingValue = "true")
public class CorsConfig {

    private static final String SPRING_VERSION = "2.4.0";

    /**
     *  是否开启,默认：false
     */
    private boolean enabled = false;

    /**
     * web flux 跨域
     *
     * @author Rong.Jia
     * @date 2021/12/20
     */
    @Configuration
    @ConditionalOnClass({WebFluxConfigurer.class})
    public class CorsWebFluxConfig {
        @Bean
        public CorsWebFilter corsFilter() {

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
            source.registerCorsConfiguration("/**", createCorsConfiguration());

            return new CorsWebFilter(source);
        }

    }

    /**
     * webmvc 跨域
     *
     * @author Rong.Jia
     * @date 2021/12/20
     */
    @Configuration
    @ConditionalOnClass({DispatcherServlet.class, WebMvcConfigurer.class})
    public class CorsWebMvcConfig {

        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
            org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", createCorsConfiguration());
            FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
            bean.setOrder(0);
            return bean;
        }
    }

    /**
     * 比较版本
     *
     * @return boolean
     */
    private boolean compareVersion() {
        String version = SpringBootVersion.getVersion();
        return VersionComparator.INSTANCE.compare(version, SPRING_VERSION) >= 0;
    }

    /**
     * 创建跨域配置
     *
     * @return {@link CorsConfiguration}
     */
    private CorsConfiguration createCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedHeaders(Collections.singletonList("Authorization,Origin, X-Requested-With, Content-Type, Accept,WWW-Authenticate"));
        config.setAllowCredentials(true);

        if (compareVersion()) {
            config.addAllowedOriginPattern("*");
        }else {
            config.addAllowedOrigin("*");
        }
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addExposedHeader("Location");

        return config;
    }















}
