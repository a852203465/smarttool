package cn.darkjrong.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

/**
 * 应用程序工具类
 *
 * @author Rong.Jia
 * @date 2024/02/29
 */
@Slf4j
public class AppUtils {

    private static Environment environment;

    /**
     * 获取应用名称
     *
     * @return {@link String}
     */
    public static String getAppName() {
        String name = getEnv().getProperty("spring.application.name");
        return StrUtil.isBlank(name) ? "default" : name;
    }




















    private static Environment getEnv() {
        if (ObjectUtil.isNull(environment)) {
            synchronized (AppUtils.class) {
                if (ObjectUtil.isNull(environment)) {
                    environment = SpringUtil.getBean(Environment.class);
                }
            }
        }
        return environment;
    }















}
