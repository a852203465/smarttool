package cn.darkjrong.captcha.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证码类型
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Getter
@AllArgsConstructor
public enum CaptchaType {

    Arithmetic,
    Chinese,
    ChineseGif,
    Gif,
    Spec,
    Slider,
    ClickWord,




    ;

    public static List<CaptchaType> preciseType() {
        List<CaptchaType> captchaTypes = new ArrayList<>();
        captchaTypes.add(Arithmetic);
        captchaTypes.add(Spec);
        captchaTypes.add(Gif);
        captchaTypes.add(Chinese);
        captchaTypes.add(ChineseGif);
        return captchaTypes;
    }







}
