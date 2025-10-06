package cn.darkjrong.captcha.factory.cap;


import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.captcha.uitls.ColorUtils;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.img.ImgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * png格式  算术  验证码
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
@Slf4j
@Component
public class ArithmeticCaptcha extends AbstractArithmeticCaptcha {

    public ArithmeticCaptcha(CaptchaProperties captchaProperties) {
        super(captchaProperties);
    }

    @Override
    public Boolean support(CaptchaType type) {
        return CaptchaType.Arithmetic.equals(type);
    }

    @Override
    protected void outImg() {
        char[] chars = getCalculationFormula().toCharArray();
        image = drawImg(chars);
    }

    @Override
    protected byte[] drawImg(char[] chars) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        // 填充背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画干扰圆
        drawOval(2, g2d);
        // 画字符串
        g2d.setFont(getFont());
        FontMetrics fontMetrics = g2d.getFontMetrics();
        // 每一个字符所占的宽度
        int fW = width / chars.length;
        // 字符的左右边距
        int fSp = (fW - (int) fontMetrics.getStringBounds("8", g2d).getWidth()) / 2;
        for (int i = 0; i < chars.length; i++) {
            g2d.setColor(ColorUtils.color());
            // 文字的纵坐标
            int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(chars[i]), g2d).getHeight()) >> 1);
            g2d.drawString(String.valueOf(chars[i]), i * fW + fSp + 3, fY - 3);
        }
        g2d.dispose();
        return ImgUtil.toBytes(bi, "png");
    }

}