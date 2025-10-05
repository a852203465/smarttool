package cn.darkjrong.xss;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xss过滤器
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
@Slf4j
public class XssFilter extends OncePerRequestFilter {

    /**
     * 存储需要排除XSS过滤的URL模式列表。
     */
    private final List<String> excludes = new ArrayList<>();

    /**
     * 初始化过滤器
     *
     */
    @Override
    protected void initFilterBean() throws ServletException {
        FilterConfig filterConfig = getFilterConfig();
        if (ObjectUtil.isNotNull(filterConfig)) {
            String strExcludes = filterConfig.getInitParameter("excludes");
            if (StrUtil.isNotEmpty(strExcludes)) {
                excludes.addAll(StrUtil.split(strExcludes, ","));
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //如果该访问接口在排除列表里面则不拦截
        if (isExcludeUrl(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("接口【{}】进行xss过滤", request.getRequestURI());
        final XssRequestWrapper requestWrapper = new XssRequestWrapper(request);
        filterChain.doFilter(requestWrapper, response);
    }

    /**
     * 销毁过滤器，释放资源。
     */
    @Override
    public void destroy() {
    }

    /**
     * 判断当前请求的URL是否应该被排除在XSS过滤之外。
     *
     * @param urlPath 请求的URL路径。
     * @return 如果请求应该被排除，则返回true；否则返回false。
     */
    private boolean isExcludeUrl(String urlPath) {
        if (CollUtil.isEmpty(excludes)) {
            return false;
        }

        String url = urlPath;
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }


}
