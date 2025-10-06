package cn.darkjrong.captcha.factory.store;

/**
 * 验证码存储
 * @author Rong.Jia
 * @date 2025/10/05
 */
public interface CaptchaStore {

    /**
     * 存储
     *
     * @param captchaId 验证码id
     * @param value     值
     */
    void store(String captchaId, String value);

    /**
     * 验证
     *
     * @param captchaId 验证码id
     * @param value     值
     * @return {@link Boolean }
     */
    Boolean verify(String captchaId, String value);


















}
