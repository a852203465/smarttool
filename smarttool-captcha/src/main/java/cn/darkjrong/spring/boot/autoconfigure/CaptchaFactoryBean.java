package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.captcha.CaptchaTemplate;
import cn.darkjrong.captcha.factory.cap.CaptchaFactory;
import cn.darkjrong.captcha.factory.store.CaptchaStore;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 验证码工厂类
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
public class CaptchaFactoryBean implements FactoryBean<CaptchaTemplate>, InitializingBean, DisposableBean {

    private CaptchaTemplate captchaTemplate;
    private final CaptchaStore captchaStore;
    private final CaptchaProperties captchaProperties;
    private final CaptchaFactory captchaFactory;

    public CaptchaFactoryBean(CaptchaProperties captchaProperties, CaptchaStore captchaStore, CaptchaFactory captchaFactory) {
        this.captchaStore = captchaStore;
        this.captchaProperties = captchaProperties;
        this.captchaFactory = captchaFactory;
    }

    @Override
    public CaptchaTemplate getObject() {
        return this.captchaTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return CaptchaTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void afterPropertiesSet() {
        this.captchaTemplate = new CaptchaTemplate(captchaProperties, captchaStore, captchaFactory);
    }

}
