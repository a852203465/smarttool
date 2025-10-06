package cn.darkjrong.captcha.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码文本类型
 *
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Getter
@AllArgsConstructor
public enum CaptchaTextType {

    /**
     * 验证码文本类型
     * 字母数字混合,纯数字,纯字母,纯大写字母,纯小写字母,数字大写字母
     */
    TYPE_DEFAULT(1),
    TYPE_ONLY_NUMBER(2),
    TYPE_ONLY_CHAR(3),
    TYPE_ONLY_UPPER(4),
    TYPE_ONLY_LOWER(5),
    TYPE_NUM_AND_UPPER(6),


    ;

    private final Integer value;



}
