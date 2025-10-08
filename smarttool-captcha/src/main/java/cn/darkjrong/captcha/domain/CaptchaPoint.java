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
     * 值
     */
    private String text;

    /**
     * x坐标
     */
    private Double x;

    /**
     * y坐标
     */
    private Double y;

    public CaptchaPoint(Double x, Double y) {
        this.y = y;
        this.x = x;
    }





}
