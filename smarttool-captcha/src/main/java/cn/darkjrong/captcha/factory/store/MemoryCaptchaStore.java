package cn.darkjrong.captcha.factory.store;

import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.core.map.TimedMap;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 内存验证码存储
 *
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Slf4j
public class MemoryCaptchaStore implements CaptchaStore {

    private final CaptchaProperties captchaProperties;
    private final TimedMap<String, String> captchaStorage = new TimedMap<>();

    public MemoryCaptchaStore(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    @Override
    public void store(String captchaId, String value) {
        captchaStorage.put(captchaId, value,
                captchaProperties.getExpDuration().toMillis(), TimeUnit.SECONDS);
    }

    @Override
    public Boolean verify(String captchaId, String value) {
        List<CaptchaType> captchaTypes = CaptchaType.preciseType();
        boolean match = captchaTypes.stream()
                .anyMatch(type -> type.equals(captchaProperties.getType()));
        String correctAnswer = captchaStorage.get(captchaId);
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
        captchaStorage.remove(captchaId);
        return Boolean.TRUE;
    }
}
