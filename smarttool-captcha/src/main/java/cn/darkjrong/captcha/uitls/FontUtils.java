package cn.darkjrong.captcha.uitls;

import cn.darkjrong.captcha.enums.FontType;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 解决自定义字体读取时，产生.tmp临时文件耗磁盘的问题。
 *
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Slf4j
public class FontUtils {

    private static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, 32);
    private static final Font ZH_FONT = new Font("楷体", Font.PLAIN, 28);
    private static final List<Font> FONTS = new ArrayList<>();

    static {
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:/font/**";
        try {
            Resource[] resources = resourceLoader.getResources(pattern);
            for (Resource resource : resources) {
                if (StrUtil.isNotBlank(resource.getFilename()) && resource.getFilename().endsWith(".ttf")) {
                    try {
                        Font font = Font.createFont(Font.TRUETYPE_FONT, resource.getInputStream())
                                .deriveFont(Font.BOLD, 32f);
                        FONTS.add(font);
                        log.info("================,Captcha,字体【{}】加载成功", font.getName());
                    } catch (Exception e) {
                        log.error(String.format("字体【%s】加载异常,【%s】", resource.getFilename(), e.getMessage()), e);
                    }
                }
            }
        } catch (IOException e) {
            log.error(String.format("**************,字体加载异常,【%s】", e.getMessage()), e);
        }
    }

    /**
     * 获取zh字体
     *
     * @return {@link Font }
     */
    public static Font getZhFont() {
        return ZH_FONT;
    }

    /**
     * 获取默认字体
     *
     * @return {@link Font }
     */
    public static Font getDefaultFont() {
        return FONTS.stream()
                .filter(a -> StrUtil.equalsIgnoreCase(a.getName(), FontType.Action_Jackson.getValue()))
                .findAny()
                .orElse(DEFAULT_FONT);
    }

    /**
     * 获取字体
     *
     * @param fontType 字体类型
     * @return {@link Font }
     */
    public static Font getFont(String fontType) {
        return FONTS.stream()
                .filter(a -> StrUtil.equalsIgnoreCase(a.getName(), fontType))
                .findAny()
                .orElse(DEFAULT_FONT);
    }

    /**
     * 获取字体
     *
     * @param fontType 字体类型
     * @param style    风格
     * @param size     尺寸
     * @return {@link Font }
     */
    public static Font getFont(String fontType, int style, float size) {
        Font font = FONTS.stream()
                .filter(a -> StrUtil.equalsIgnoreCase(a.getName(), fontType))
                .findAny()
                .orElse(DEFAULT_FONT);
        return font.deriveFont(style, size);
    }

    /**
     * 获取字体
     *
     * @param fontType 字体类型
     * @param size     尺寸
     * @return {@link Font }
     */
    public static Font getFont(String fontType, float size) {
        Font font = FONTS.stream()
                .filter(a -> StrUtil.equalsIgnoreCase(a.getName(), fontType))
                .findAny()
                .orElse(DEFAULT_FONT);
        return font.deriveFont(size);
    }

    /**
     * 随机
     *
     * @return {@link Font }
     */
    public static Font random() {
        return FONTS.get(RandomUtil.randomInt(FONTS.size()));
    }


}
