package cn.darkjrong.captcha.domain;

import lombok.Data;

import java.util.List;

/**
 * 验证码信息
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Data
public class CaptchaCode {

    /**
     * 验证码唯一ID
     */
    private String captchaId;

    /**
     *  滑块图
     */
    private String sliderImg;

    /**
     * 原图
     */
    private String srcImg;

    /**
     * 文本验证码
     */
    private String text;

    /**
     *  宽
     */
    private Integer xWidth;

    /**
     *  高
     */
    private Integer yHeight;

    /**
     * 图片类型
     */
    private String contentType;

    /**
     * 坐标
     */
    private List<CaptchaPoint> points;








}
