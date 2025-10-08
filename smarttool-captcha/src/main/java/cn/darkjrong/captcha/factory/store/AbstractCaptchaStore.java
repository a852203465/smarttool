package cn.darkjrong.captcha.factory.store;

import cn.darkjrong.captcha.domain.CaptchaPoint;
import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        } else if (CaptchaType.Slider.equals(captchaProperties.getType())) {
            Double xPos = Double.parseDouble(value);
            Double vCode = Double.parseDouble(correctAnswer);
            if (xPos - vCode > 5 || xPos - vCode < - 5) {
                log.error("验证码ID【{}】输入【{}】答案【{}】验证不通过", captchaId, xPos, vCode);
                return Boolean.FALSE;
            }
        } else if (CaptchaType.ClickWord.equals(captchaProperties.getType())) {
            List<CaptchaPoint> captchaPoints = JSONObject.parseObject(value, new TypeReference<List<CaptchaPoint>>() {});
            List<CaptchaPoint> correctAnswers = JSONObject.parseObject(correctAnswer, new TypeReference<List<CaptchaPoint>>() {});

            if (CollUtil.isEmpty(captchaPoints) || CollUtil.isEmpty(correctAnswers)) {
                log.error("验证码ID【{}】输入【{}】或者答案【{}】为空", captchaId, value, correctAnswer);
                return Boolean.FALSE;
            }
            Map<String, CaptchaPoint> captchaPointMap = captchaPoints.stream()
                    .collect(Collectors.toMap(CaptchaPoint::getText, a -> a));
            Map<String, CaptchaPoint> correctAnswerMap = correctAnswers.stream()
                    .collect(Collectors.toMap(CaptchaPoint::getText, a -> a));

            for (Map.Entry<String, CaptchaPoint> entry : correctAnswerMap.entrySet()) {
                String key = entry.getKey();
                CaptchaPoint correctAnswerPoint = entry.getValue();
                CaptchaPoint captchaPoint = captchaPointMap.get(key);
                if (ObjectUtil.isNull(captchaPoint)) {
                    log.error("验证码ID【{}】文本【{}】没有对应的答案", captchaId, key);
                    return Boolean.FALSE;
                }
                Double cX = correctAnswerPoint.getX();
                Double cY = correctAnswerPoint.getY();
                Double y = captchaPoint.getY();
                Double x = captchaPoint.getX();
                if (x - cX > 5 || x - cX < - 5) {
                    log.error("验证码ID【{}】横坐标,输入【{}】答案【{}】验证不通过", captchaId, x, cX);
                    return Boolean.FALSE;
                }
                if (y - cY > 5 || y - cY < - 5) {
                    log.error("验证码ID【{}】纵坐标,输入【{}】答案【{}】验证不通过", captchaId, y, cY);
                    return Boolean.FALSE;
                }
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
