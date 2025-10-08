package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.domain.CaptchaPoint;
import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.captcha.uitls.ChineseUtils;
import cn.darkjrong.captcha.uitls.FontUtils;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.collection.CollectionUtil;
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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 点选验证码
 *
 * @author Rong.Jia
 * @date 2025/10/08
 */
@Slf4j
@Component
public class ClickWordCaptcha extends AbstractImgCaptcha {

    private static final List<String> IMG_SUFFIX = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final List<byte[]> srcList = new ArrayList<>();

    static {
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        String srcs = "classpath*:/imgs/img-clicks/**";
        try {
            Resource[] resources = resourceLoader.getResources(srcs);
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (StrUtil.isNotBlank(fileName)
                        && IMG_SUFFIX.stream().anyMatch(a -> StrUtil.endWith(fileName, a))) {
                    try {
                        byte[] bytes = IoUtil.readBytes(resource.getInputStream());
                        srcList.add(bytes);
                        log.debug("================,ClickWord,原图【{}】加载成功", fileName);
                    } catch (Exception e) {
                        log.error(String.format("ClickWord【%s】加载异常,【%s】", fileName, e.getMessage()), e);
                    }
                }
            }
        } catch (IOException e) {
            log.error(String.format("**************,ClickWord,原图【%s】加载异常,【%s】", srcs, e.getMessage()), e);
        }
    }

    public ClickWordCaptcha(CaptchaProperties captchaProperties) {
        super(captchaProperties);
    }

    @Override
    public Boolean support(CaptchaType type) {
        return CaptchaType.ClickWord.equals(type);
    }

    @Override
    public CaptchaCode out() {
        byte[] src = srcList.get(RandomUtil.randomInt(srcList.size()));
        ClickWord clickWord = getImageData(src);

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCaptchaId(IdUtil.fastSimpleUUID());
        captchaCode.setSrcImg(toBase64(clickWord.getImg()));
        captchaCode.setText(CollectionUtil.join(clickWord.getWords(), StrUtil.EMPTY));
        captchaCode.setPoints(clickWord.getPoints());
        captchaCode.setContentType(getContentType());
        return captchaCode;
    }

    private ClickWord getImageData(byte[] src) {
        Font font = FontUtils.getZhFont();
        List<String> wordList = new ArrayList<>();
        List<CaptchaPoint> pointList = new ArrayList<>();

        BufferedImage bufferedImage = ImgUtil.toImage(src);
        Graphics backgroundGraphics = bufferedImage.getGraphics();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        Integer wordCount = captchaProperties.getClickWord().getClickCount();

        //定义随机1到arr.length某一个字不参与校验
        int num = RandomUtil.randomInt(1, wordCount);
        List<String> currentWords = ChineseUtils.random(wordCount);

        int i = 0;
        for (String word : currentWords) {
            //随机字体坐标
            CaptchaPoint point = randomWordPoint(width, height, i, wordCount);
            //随机字体颜色
            if (captchaProperties.getClickWord().getFontColorRandom()) {
                backgroundGraphics.setColor(new Color(RandomUtil.randomInt(1, 255),
                        RandomUtil.randomInt(1, 255), RandomUtil.randomInt(1, 255)));
            } else {
                backgroundGraphics.setColor(Color.BLACK);
            }

            //设置角度
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(RandomUtil.randomInt(-45, 45)), 0, 0);
            Font rotatedFont = font.deriveFont(affineTransform);
            backgroundGraphics.setFont(rotatedFont);
            backgroundGraphics.drawString(word, Convert.toInt(point.getX()), Convert.toInt(point.getY()));

            if ((num - 1) != i) {
                wordList.add(word);
                point.setText(word);
                pointList.add(point);
            }
            i++;
        }

        backgroundGraphics.setFont(font);
        backgroundGraphics.setColor(Color.white);
//        backgroundGraphics.drawString(waterMark, width - getEnOrChLength(waterMark), height - (HAN_ZI_SIZE / 2) + 7);

        //创建合并图片
        BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics combinedGraphics = combinedImage.getGraphics();
        combinedGraphics.drawImage(bufferedImage, 0, 0, null);

        ClickWord clickWord = new ClickWord();
        clickWord.setWords(wordList);
        clickWord.setPoints(pointList);
        clickWord.setImg(ImgUtil.toBytes(bufferedImage, ImgUtil.IMAGE_TYPE_PNG));
        return clickWord;
    }

    /**
     * 随机字体循环排序下标
     *
     * @param imageWidth    图片宽度
     * @param imageHeight   图片高度
     * @param wordSortIndex 字体循环排序下标(i)
     * @param wordCount     字数量
     * @return {@link CaptchaPoint }
     */
    private static CaptchaPoint randomWordPoint(int imageWidth, int imageHeight, int wordSortIndex, int wordCount) {
        int avgWidth = imageWidth / (wordCount + 1);
        int size = 25;
        int half = size / 2;
        int x, y;
        if (avgWidth < half) {
            x = RandomUtil.randomInt(1 + half, imageWidth);
        } else {
            if (wordSortIndex == 0) {
                x = RandomUtil.randomInt(1 + half, avgWidth * (wordSortIndex + 1) - half);
            } else {
                x = RandomUtil.randomInt(avgWidth * wordSortIndex + half, avgWidth * (wordSortIndex + 1) - half);
            }
        }
        y = RandomUtil.randomInt(size, imageHeight - size);
        return new CaptchaPoint(Convert.toDouble(x), Convert.toDouble(y));
    }

    @Data
    private static class ClickWord {
        private List<String> words;
        private List<CaptchaPoint> points;
        private byte[] img;
    }








}
