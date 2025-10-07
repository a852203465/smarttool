package cn.darkjrong.captcha.factory.store;

import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;

/**
 * 抽象验证码存储
 *
 * @author Rong.Jia
 * @date 2025/10/07
 */
@Slf4j
public abstract class AbstractCaptchaStore implements CaptchaStore {

    @Autowired
    protected CaptchaProperties captchaProperties;

    /**
     * 存储
     *
     * @param captchaId   验证码ID
     * @param value       值
     * @param expDuration 过期时间
     */
    public abstract void store(String captchaId, String value, Duration expDuration);

    /**
     * 移除
     *
     * @param captchaId 验证码id
     */
    public abstract void remove(String captchaId);

    /**
     * 获取
     *
     * @param captchaId 验证码id
     * @return {@link String }
     */
    public abstract String get(String captchaId);

    @Override
    public Boolean verify(String captchaId, String value) {
        List<CaptchaType> captchaTypes = CaptchaType.preciseType();
        boolean match = captchaTypes.stream()
                .anyMatch(type -> type.equals(captchaProperties.getType()));
        String correctAnswer = get(captchaId);
        if (StrUtil.isBlank(correctAnswer)) {
            log.error("验证码ID【{}】的答案为空", captchaId);
            return Boolean.FALSE;
        }

        if (match) {
            if (!StrUtil.equals(value, correctAnswer)) {
                log.error("验证码ID【{}】输入【{}】答案【{}】不一致", captchaId, value, correctAnswer);
                return Boolean.FALSE;
            }
        } else  {
            Double xPos = Double.parseDouble(value);
            Double vCode = Double.parseDouble(correctAnswer);
            if (xPos - vCode > 5 || xPos - vCode < - 5) {
                log.error("验证码ID【{}】输入【{}】答案【{}】验证不通过", captchaId, xPos, vCode);
                return Boolean.FALSE;
            }
        }
        remove(captchaId);
        return Boolean.TRUE;
    }

    @Override
    public void store(String captchaId, String value) {
        store(captchaId, value, captchaProperties.getExpDuration());
    }


















}
