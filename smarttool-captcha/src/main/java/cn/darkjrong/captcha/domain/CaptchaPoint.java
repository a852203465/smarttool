package cn.darkjrong.captcha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码点
 *
 * @author Rong.Jia
 * @date 2025/10/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaPoint {

    /**
     * x坐标
     */
    private Integer x;

    /**
     * y坐标
     */
    private Integer y;




}
