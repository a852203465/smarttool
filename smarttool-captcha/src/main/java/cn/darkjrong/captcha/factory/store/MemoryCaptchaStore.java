package cn.darkjrong.captcha.factory.store;

import cn.darkjrong.core.map.TimedMap;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 内存验证码存储
 *
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Slf4j
public class MemoryCaptchaStore extends AbstractCaptchaStore {

    private final TimedMap<String, String> captchaStorage = new TimedMap<>();

    @Override
    public void store(String captchaId, String value, Duration expDuration) {
        captchaStorage.put(captchaId, value, expDuration.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void remove(String captchaId) {
        captchaStorage.remove(captchaId);
    }

    @Override
    public String get(String captchaId) {
        return captchaStorage.get(captchaId);
    }




}
