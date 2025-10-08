package cn.darkjrong.captcha;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.captcha.factory.cap.CaptchaFactory;
import cn.darkjrong.captcha.factory.store.CaptchaStore;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码操作
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Slf4j
public class CaptchaTemplate {

    private final CaptchaProperties captchaProperties;
    private final CaptchaStore captchaStore;
    private final CaptchaFactory captchaFactory;

    public CaptchaTemplate(CaptchaProperties captchaProperties, CaptchaStore captchaStore, CaptchaFactory captchaFactory) {
        this.captchaProperties = captchaProperties;
        this.captchaStore = captchaStore;
        this.captchaFactory = captchaFactory;
    }

    /**
     * 生成验证码
     * @return {@link CaptchaCode }
     */
    public CaptchaCode genCaptcha() {
        CaptchaCode captcha = captchaFactory.getCaptcha();
        String uuid = IdUtil.fastSimpleUUID();
        captcha.setCaptchaId(uuid);
        String value = captchaProperties.getType().equals(CaptchaType.ClickWord)
                ? JSON.toJSONString(captcha.getPoints())
                : captcha.getText();
        captchaStore.store(uuid, value);
        return captcha;
    }

    /**
     * 验证验证码
     *
     * @param captchaId 验证码id
     * @param value     值
     * @return {@link Boolean }
     */
    public Boolean verifyCaptcha(String captchaId, String value) {
        return captchaStore.verify(captchaId, value);
    }













}
