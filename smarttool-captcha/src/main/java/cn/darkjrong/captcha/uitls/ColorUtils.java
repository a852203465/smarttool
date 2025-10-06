package cn.darkjrong.captcha.uitls;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * 颜色工具类
 *
 * @author Rong.Jia
 * @date 2025/10/06
 */
@Slf4j
public class ColorUtils {

    /**
     * 常用颜色
     */
    private static final int[][] COLOR = {
            {0, 135, 255},
            {51, 153, 51},
            {255, 102, 102},
            {255, 153, 0},
            {153, 102, 0},
            {153, 102, 153},
            {51, 153, 153},
            {102, 102, 255},
            {0, 102, 204},
            {204, 51, 51},
            {0, 153, 204},
            {0, 51, 102}
    };

    /**
     * 给定范围获得随机颜色
     *
     * @param fc 0-255
     * @param bc 0-255
     * @return 随机颜色
     */
    public static Color color(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + Randoms.num(bc - fc);
        int g = fc + Randoms.num(bc - fc);
        int b = fc + Randoms.num(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 获取随机常用颜色
     *
     * @return 随机颜色
     */
    public static Color color() {
        int[] color = COLOR[Randoms.num(COLOR.length)];
        return new Color(color[0], color[1], color[2]);
    }






}
