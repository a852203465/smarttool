package cn.darkjrong.captcha.uitls;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 中文工具类
 *
 * @author Rong.Jia
 * @date 2025/10/08
 */
@Slf4j
public class ChineseUtils {

    private static final List<String> HAN_ZIS = new ArrayList<>();

    static {
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:/chars/**";
        try {
            Resource[] resources = resourceLoader.getResources(pattern);
            for (Resource resource : resources) {
                if (StrUtil.isNotBlank(resource.getFilename())
                        && StrUtil.endWith(resource.getFilename(), "chinese_chars.txt")) {
                    try {
                        List<String> hans = IoUtil.readLines(resource.getInputStream(),
                                CharsetUtil.CHARSET_UTF_8, CollectionUtil.newArrayList());
                        HAN_ZIS.addAll(hans);
                        log.debug("================,常见汉字【{}】加载成功", resource.getFilename());
                    } catch (Exception e) {
                        log.error(String.format("常见汉字【%s】加载异常,【%s】", resource.getFilename(), e.getMessage()), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error(String.format("**************, 常见汉字加载异常,【%s】", e.getMessage()), e);
        }
    }

    /**
     * 随机汉字
     *
     * @return 随机汉字
     */
    public static String random() {
        return HAN_ZIS.get(Randoms.num(HAN_ZIS.size()));
    }


    /**
     * 随机汉字
     *
     * @param size 数量
     * @return {@link List }<{@link String }>
     */
    public static List<String> random(Integer size) {
        Set<String> words = new HashSet<>();
        do {
            String t = HAN_ZIS.get(RandomUtil.randomInt(HAN_ZIS.size()));
            words.add(t);
        } while (words.size() < size);
        return CollUtil.newArrayList(words);
    }

    /**
     * 个数
     *
     * @return {@link Integer }
     */
    public static Integer size() {
        return HAN_ZIS.size();
    }









}
