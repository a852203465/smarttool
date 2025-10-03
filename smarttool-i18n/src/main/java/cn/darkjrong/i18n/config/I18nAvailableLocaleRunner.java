package cn.darkjrong.i18n.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 可用多语言输出
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Slf4j
//@Component
@AllArgsConstructor
public class I18nAvailableLocaleRunner implements ApplicationRunner {

    private final Environment env;

    @Override
    public void run(ApplicationArguments args) {
        StringJoiner joiner = new StringJoiner("\n\t");
        List<List<Locale>> splits = CollUtil.split(CollectionUtil.newArrayList(Locale.getAvailableLocales()), 8);
        for (int i = 0; i < splits.size(); i++) {
            List<Locale> locales = splits.get(i);
            if (i == 0) {
                joiner.add(CollectionUtil.join(locales.stream()
                        .filter(a -> StrUtil.isNotBlank(a.toString()))
                        .collect(Collectors.toList()), ","));
            } else {
                joiner.add("\t\t\t" + CollectionUtil.join(locales.stream()
                        .filter(a -> StrUtil.isNotBlank(a.toString()))
                        .collect(Collectors.toList()), ","));
            }
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application support Locales\n\t" +
                        "Locales: {}\n\t" +
                        "----------------------------------------------------------",
                joiner.toString());
    }
}
