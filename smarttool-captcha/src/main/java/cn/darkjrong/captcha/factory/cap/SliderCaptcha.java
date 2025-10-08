package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 滑块验证码
 *
 * @author Rong.Jia
 * @date 2025/10/07
 */
@Slf4j
@Component
public class SliderCaptcha extends AbstractImgCaptcha {

    private static final int BOLD = 5;
    private static final List<String> IMG_SUFFIX = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final List<byte[]> cutList = new ArrayList<>();
    private static final List<byte[]> srcList = new ArrayList<>();

    static {
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        String cuts = "classpath*:/imgs/slider/cuts/**";
        String srcs = "classpath*:/imgs/slider/srcs/**";
        try {
            Resource[] resources = resourceLoader.getResources(cuts);
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (StrUtil.isNotBlank(fileName)
                        && IMG_SUFFIX.stream().anyMatch(a -> StrUtil.endWith(fileName, a))) {
                    try {
                        byte[] bytes = IoUtil.readBytes(resource.getInputStream());
                        cutList.add(bytes);
                        log.debug("================,Slider,切图【{}】加载成功", fileName);
                    } catch (Exception e) {
                        log.error(String.format("Slider【%s】加载异常,【%s】", fileName, e.getMessage()), e);
                    }
                }
            }
        } catch (IOException e) {
            log.error(String.format("**************,Slider,切图【%s】加载异常,【%s】", cuts, e.getMessage()), e);
        }
        try {
            Resource[] resources = resourceLoader.getResources(srcs);
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (StrUtil.isNotBlank(fileName)
                        && IMG_SUFFIX.stream().anyMatch(a -> StrUtil.endWith(fileName, a))) {
                    try {
                        byte[] bytes = IoUtil.readBytes(resource.getInputStream());
                        srcList.add(bytes);
                        log.debug("================,Slider,原图【{}】加载成功", fileName);
                    } catch (Exception e) {
                        log.error(String.format("Slider【%s】加载异常,【%s】", fileName, e.getMessage()), e);
                    }
                }
            }
        } catch (IOException e) {
            log.error(String.format("**************,Slider,原图【%s】加载异常,【%s】", srcs, e.getMessage()), e);
        }
    }

    public SliderCaptcha(CaptchaProperties captchaProperties) {
        super(captchaProperties);
    }

    @Override
    public Boolean support(CaptchaType type) {
        return CaptchaType.Slider.equals(type);
    }

    @Override
    public CaptchaCode out() {
        byte[] src = srcList.get(RandomUtil.randomInt(srcList.size()));
        byte[] cut = cutList.get(RandomUtil.randomInt(cutList.size()));
        Slider slider = cutImg(src, cut);

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCaptchaId(IdUtil.fastSimpleUUID());
        captchaCode.setSrcImg(toBase64(slider.getSrc()));
        captchaCode.setSliderImg(toBase64(slider.getCut()));
        captchaCode.setYHeight(slider.yHeight);
        captchaCode.setXWidth(slider.xWidth);
        captchaCode.setText(Convert.toStr(slider.xWidth));
        captchaCode.setContentType(getContentType());
        return captchaCode;
    }

    /**
     * 根据模板切图
     *
     * @param src 原图
     * @param cut 切图
     * @return {@link Slider }
     */
    private static Slider cutImg(byte[] src, byte[] cut) {

        // 模板图
        BufferedImage imageTemplate = ImgUtil.toImage(cut);
        int templateWidth = imageTemplate.getWidth();
        int templateHeight = imageTemplate.getHeight();

        // 原图
        BufferedImage oriImage = ImgUtil.toImage(src);
        int oriImageWidth = oriImage.getWidth();
        int oriImageHeight = oriImage.getHeight();

        // 随机生成抠图坐标X,Y
        // X轴距离右端targetWidth Y轴距离底部targetHeight以上
        Random random = new Random();
        int widthRandom = random.nextInt(oriImageWidth - 2 * templateWidth) + templateWidth;

        // int heightRandom = 1;
        int heightRandom = random.nextInt(oriImageHeight - templateHeight);

        log.info("原图大小{} x {},随机生成的坐标 X,Y 为（{}，{}）", oriImageWidth, oriImageHeight, widthRandom, heightRandom);

        // 新建一个和模板一样大小的图像，TYPE_4BYTE_ABGR表示具有8位RGBA颜色分量的图像，正常取imageTemplate.getType()
        BufferedImage newImage = new BufferedImage(templateWidth, templateHeight, imageTemplate.getType());

        // 得到画笔对象
        Graphics2D graphics = newImage.createGraphics();

        // 如果需要生成RGB格式，需要做如下配置,Transparency 设置透明
        newImage = graphics.getDeviceConfiguration().createCompatibleImage(templateWidth, templateHeight,
                Transparency.TRANSLUCENT);

        // 新建的图像根据模板颜色赋值,源图生成遮罩
        cutByTemplate(oriImage, imageTemplate, newImage, widthRandom, heightRandom);

        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(BOLD, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newImage, 0, 0, null);
        graphics.dispose();

        byte[] newImageByte = ImgUtil.toBytes(newImage, ImgUtil.IMAGE_TYPE_PNG);
        byte[] oriImageByte = ImgUtil.toBytes(oriImage, ImgUtil.IMAGE_TYPE_JPG);

        Slider slider = new Slider();
        slider.setSrc(oriImageByte);
        slider.setCut(newImageByte);
        slider.setXWidth(widthRandom);
        slider.setYHeight(heightRandom);
        return slider;
    }

    /**
     * 切图
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param newImage      新抠出的小图
     * @param x             随机扣取坐标X
     * @param y             随机扣取坐标y
     */
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage, BufferedImage newImage, int x, int y) {

        // 临时数组遍历用于高斯模糊存周边像素值
        int[][] martrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    newImage.setRGB(i, j, oriImage.getRGB(x + i, y + j));

                    // 抠图区域高斯模糊
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(martrix));
                }

                // 防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);

                // 描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if ((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0)
                        || (rgb < 0 && downRgb >= 0)) {
                    newImage.setRGB(i, j, Color.white.getRGB());
                    oriImage.setRGB(x + i, y + j, Color.white.getRGB());
                }
            }
        }
    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for (int i = xStart; i < 3 + xStart; i++)
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;

                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);

            }
    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }

    @Data
    private static class Slider {
        private byte[] src;
        private byte[] cut;
        private Integer xWidth;
        private Integer yHeight;
    }



}
