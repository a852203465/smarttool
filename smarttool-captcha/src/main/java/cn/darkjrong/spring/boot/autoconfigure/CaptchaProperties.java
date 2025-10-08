package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.captcha.enums.ArithmeticType;
import cn.darkjrong.captcha.enums.CaptchaTextType;
import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.captcha.enums.FontType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 验证码属性
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.captcha")
public class CaptchaProperties {

    /**
     * 验证码类型,默认:Spec
     */
    private CaptchaType type = CaptchaType.Spec;

    /**
     * 默认长度，默认值： 5
     */
    private Integer length = 5;

    /**
     * 默认宽度，默认值： 130
     */
    private Integer width = 130;

    /**
     * 默认高度，默认值：48
     */
    private Integer height = 48;

    /**
     * 过期时长,默认5分钟
     */
    private Duration expDuration = Duration.ofMinutes(5);

    /**
     * 文本组合类型
     */
    private CaptchaTextType textType = CaptchaTextType.TYPE_DEFAULT;

    /**
     * 字体
     */
    private FontProperties font = new FontProperties();

    /**
     * 算术
     */
    private ArithmeticAlgorithm arithmetic = new ArithmeticAlgorithm();

    /**
     * 文字点选
     */
    private ClickWord clickWord = new ClickWord();

    @Data
    public static class FontProperties {

        /**
         * 字体类型
         */
        private FontType fontType = FontType.Action_Jackson;

        /**
         * 字体大小
         */
        private float size = 32f;

    }

    @Data
    public static class ArithmeticAlgorithm {

        /**
         * 算术算法
         */
        private ArithmeticType algorithm = ArithmeticType.ADD_SUB_MUL_DIV;

        /**
         * 难度,默认：10
         */
        private Integer difficulty = 10;


        public Integer getDifficulty() {
            return difficulty <= 0 ? 10 : difficulty;
        }
    }

    @Data
    public static class ClickWord {

        /**
         * 点选文字个数,默认:4
         */
        private Integer clickCount = 4;

        /**
         * 字体颜色是否随机,默认:true
         */
        private Boolean fontColorRandom = Boolean.TRUE;

    }

















}
