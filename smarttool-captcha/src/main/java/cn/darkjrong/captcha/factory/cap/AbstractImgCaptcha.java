package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;

/**
 * 抽象img验证码
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
public abstract class AbstractImgCaptcha extends AbstractCaptcha {

    public AbstractImgCaptcha(CaptchaProperties captchaProperties) {
        super(captchaProperties);
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

    @Override
    protected byte[] drawImg(char[] chars) {
        return null;
    }

}
