package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.enums.CaptchaType;

/**
 * 验证码
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
public interface Captcha {

    /**
     * 支持类型
     *
     * @param type 类型
     * @return {@link Boolean }
     */
    Boolean support(CaptchaType type);

    /**
     * 验证码输出
     *
     * @return {@link CaptchaCode }
     */
    CaptchaCode out();





















}