package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 验证码工厂
 *
 * @author Rong.Jia
 * @date 2025/10/06
 */
@Slf4j
@Component
public class CaptchaFactory {

    private final List<Captcha> captchas;
    private final CaptchaProperties captchaProperties;
    private final SpecCaptcha specCaptcha;

    public CaptchaFactory(List<Captcha> captchas, CaptchaProperties captchaProperties,
                          SpecCaptcha specCaptcha) {
        this.captchas = captchas;
        this.captchaProperties = captchaProperties;
        this.specCaptcha = specCaptcha;
    }

    /**
     * 生成验证码
     * @return {@link CaptchaCode }
     */
    public CaptchaCode getCaptcha(CaptchaType type) {
        Captcha captcha = captchas.stream()
                .filter(a -> a.support(type))
                .findAny()
                .orElse(specCaptcha);
        return captcha.out();
    }


    /**
     * 生成验证码
     * @return {@link CaptchaCode }
     */
    public CaptchaCode getCaptcha() {
        Captcha captcha = captchas.stream()
                .filter(a -> a.support(captchaProperties.getType()))
                .findAny()
                .orElse(specCaptcha);
        return captcha.out();
    }


























}
