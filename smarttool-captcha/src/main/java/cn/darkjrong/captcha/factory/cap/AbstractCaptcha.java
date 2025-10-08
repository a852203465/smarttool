package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.enums.ArithmeticType;
import cn.darkjrong.captcha.enums.CaptchaTextType;
import cn.darkjrong.captcha.uitls.ColorUtils;
import cn.darkjrong.captcha.uitls.FontUtils;
import cn.darkjrong.captcha.uitls.Randoms;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;

/**
 * 抽象验证码
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
public abstract class AbstractCaptcha implements Captcha {

    protected final CaptchaProperties captchaProperties;

    @Getter
    private Font font = new Font("Arial", Font.BOLD, 32);
    protected int len = 5;
    protected int width = 130;
    protected int height = 48;
    protected CaptchaTextType charType = CaptchaTextType.TYPE_DEFAULT;
    protected ArithmeticType arithmeticType;
    protected Integer difficulty = 10;
    protected String chars;
    protected byte[] image;

    public AbstractCaptcha(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
        this.len = captchaProperties.getLength();
        this.width = captchaProperties.getWidth();
        this.height = captchaProperties.getHeight();
        this.charType = captchaProperties.getTextType();

        CaptchaProperties.ArithmeticAlgorithm arithmetic = captchaProperties.getArithmetic();
        this.arithmeticType = arithmetic.getAlgorithm();
        this.difficulty = arithmetic.getDifficulty();

        CaptchaProperties.FontProperties fontProperties = captchaProperties.getFont();
        this.font = FontUtils.getFont(fontProperties.getFontType().getValue(), fontProperties.getSize());
    }

    protected abstract String getContentType();

    @Override
    public CaptchaCode out() {
        alphas();
        outImg();
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCaptchaId(IdUtil.fastSimpleUUID());
        captchaCode.setSrcImg(toBase64());
        captchaCode.setText(chars);
        captchaCode.setContentType(getContentType());
        return captchaCode;
    }

    /**
     * 生成随机验证码
     */
    protected void alphas() {
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            Integer value = charType.getValue();
            switch (value) {
                case 2:
                    cs[i] = Randoms.alpha(Randoms.numMaxIndex);
                    break;
                case 3:
                    cs[i] = Randoms.alpha(Randoms.charMinIndex, Randoms.charMaxIndex);
                    break;
                case 4:
                    cs[i] = Randoms.alpha(Randoms.upperMinIndex, Randoms.upperMaxIndex);
                    break;
                case 5:
                    cs[i] = Randoms.alpha(Randoms.lowerMinIndex, Randoms.lowerMaxIndex);
                    break;
                case 6:
                    cs[i] = Randoms.alpha(Randoms.upperMaxIndex);
                    break;
                default:
                    cs[i] = Randoms.alpha();
            }
        }
        chars = new String(cs);
    }

    /**
     * 验证码图片
     *
     */
    protected void outImg() {
        image = drawImg(textChar());
    }

    /**
     * 绘制img
     *
     * @return {@link byte[] }
     */
    protected abstract byte[] drawImg(char[] chars);

    protected String toBase64() {
        return Base64.encode(image);
    }

    protected String toBase64(byte[] bytes) {
        return Base64.encode(bytes);
    }

    /**
     * 获取当前验证码的字符数组
     *
     * @return 字符数组
     */
    public char[] textChar() {
        return chars.toCharArray();
    }

    /**
     * 随机画干扰线
     *
     * @param num 数量
     * @param g   Graphics2D
     */
    public void drawLine(int num, Graphics2D g) {
        drawLine(num, null, g);
    }

    /**
     * 随机画干扰线
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    public void drawLine(int num, Color color, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? ColorUtils.color() : color);
            int x1 = Randoms.num(-10, width - 10);
            int y1 = Randoms.num(5, height - 5);
            int x2 = Randoms.num(10, width + 10);
            int y2 = Randoms.num(2, height - 2);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 随机画干扰圆
     *
     * @param num 数量
     * @param g   Graphics2D
     */
    public void drawOval(int num, Graphics2D g) {
        drawOval(num, null, g);
    }

    /**
     * 随机画干扰圆
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    public void drawOval(int num, Color color, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? ColorUtils.color() : color);
            int w = 5 + Randoms.num(10);
            g.drawOval(Randoms.num(width - 25), Randoms.num(height - 15), w, w);
        }
    }

    /**
     * 随机画贝塞尔曲线
     *
     * @param num 数量
     * @param g   Graphics2D
     */
    public void drawBesselLine(int num, Graphics2D g) {
        drawBesselLine(num, null, g);
    }

    /**
     * 随机画贝塞尔曲线
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    public void drawBesselLine(int num, Color color, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? ColorUtils.color() : color);
            int x1 = 5, y1 = Randoms.num(5, height / 2);
            int x2 = width - 5, y2 = Randoms.num(height / 2, height - 5);
            int ctrlx = Randoms.num(width / 4, width / 4 * 3), ctrly = Randoms.num(5, height - 5);
            if (Randoms.num(2) == 0) {
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            if (Randoms.num(2) == 0) {  // 二阶贝塞尔曲线
                QuadCurve2D shape = new QuadCurve2D.Double();
                shape.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
                g.draw(shape);
            } else {  // 三阶贝塞尔曲线
                int ctrlx1 = Randoms.num(width / 4, width / 4 * 3), ctrly1 = Randoms.num(5, height - 5);
                CubicCurve2D shape = new CubicCurve2D.Double(x1, y1, ctrlx, ctrly, ctrlx1, ctrly1, x2, y2);
                g.draw(shape);
            }
        }
    }


}
