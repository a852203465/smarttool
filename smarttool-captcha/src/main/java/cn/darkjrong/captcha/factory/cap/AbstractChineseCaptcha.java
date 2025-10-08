package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.captcha.uitls.ChineseUtils;
import cn.darkjrong.captcha.uitls.FontUtils;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;

import java.awt.*;

/**
 * 中文验证码抽象类
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
public abstract class AbstractChineseCaptcha extends AbstractCaptcha {

    public AbstractChineseCaptcha(CaptchaProperties captchaProperties) {
        super(captchaProperties);
    }

    @Override
    public Font getFont() {
        return FontUtils.getZhFont();
    }

    @Override
    protected void alphas() {
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            cs[i] = ChineseUtils.random().charAt(0);
        }
        chars = new String(cs);
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

}
