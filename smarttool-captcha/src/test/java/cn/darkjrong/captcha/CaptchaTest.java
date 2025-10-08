package cn.darkjrong.captcha;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.enums.ArithmeticType;
import cn.darkjrong.captcha.enums.CaptchaType;
import cn.darkjrong.captcha.enums.FontType;
import cn.darkjrong.captcha.factory.cap.*;
import cn.darkjrong.captcha.uitls.FontUtils;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

@Slf4j
public class CaptchaTest {

	private static final Integer DEFAULT_IMAGE_WIDTH = 200;
	private static final Integer DEFAULT_IMAGE_HEIGHT = 60;
	private static CaptchaProperties captchaProperties;

	@BeforeEach
	public void init() {
		captchaProperties = new CaptchaProperties();
		captchaProperties.setHeight(100);
		captchaProperties.setWidth(300);
		CaptchaProperties.FontProperties font = new CaptchaProperties.FontProperties();
		captchaProperties.setFont(font);
	}


	@Test
	public void font() {
		Font defaultFont = FontUtils.getDefaultFont();
		System.out.println(defaultFont.getName());

	}

	@Test
	public void testMath() {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(captchaProperties);
		CaptchaCode captchaCode = captcha.out();
		log.info("text {}", captchaCode.getText());
	}
	@Test
	public void test() {
		CaptchaProperties.FontProperties font = new CaptchaProperties.FontProperties();
		font.setFontType(FontType.Action_Jackson);
		font.setSize(32f);
		captchaProperties.setFont(font);
		SpecCaptcha specCaptcha = new SpecCaptcha(captchaProperties);
		CaptchaCode captchaCode = specCaptcha.out();
		log.info(captchaCode.getText());
	}

	@Test
	public void testGIf() {
		CaptchaProperties.FontProperties font = new CaptchaProperties.FontProperties();
		captchaProperties.setLength(5);
		font.setFontType(FontType.Action_Jackson);
		font.setSize(32f);
		captchaProperties.setFont(font);
		GifCaptcha gifCaptcha = new GifCaptcha(captchaProperties);
		CaptchaCode captchaCode = gifCaptcha.out();
		log.info(captchaCode.getText());
	}

	@Test
	public void testHan() {
		ChineseCaptcha chineseCaptcha = new ChineseCaptcha(captchaProperties);
		CaptchaCode captchaCode = chineseCaptcha.out();
		log.info(captchaCode.getText());
	}

	@Test
	public void testGifHan() {
		ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha(captchaProperties);
		CaptchaCode captchaCode = chineseGifCaptcha.out();
		log.info(captchaCode.getText());
	}

	@Test
	public void testArit() {
		CaptchaProperties.FontProperties font = new CaptchaProperties.FontProperties();
		captchaProperties.setLength(3);
		font.setFontType(FontType.Action_Jackson);
		font.setSize(28f);
		captchaProperties.setFont(font);

		CaptchaProperties.ArithmeticAlgorithm algorithm = new CaptchaProperties.ArithmeticAlgorithm();
		algorithm.setAlgorithm(ArithmeticType.ADD_SUB_MUL_DIV);
		algorithm.setDifficulty(50);
		captchaProperties.setArithmetic(algorithm);
		ArithmeticCaptcha specCaptcha = new ArithmeticCaptcha(captchaProperties);
		CaptchaCode captchaCode = specCaptcha.out();
		log.info(specCaptcha.getCalculationFormula() + " " + captchaCode.getText());
	}
	
	@Test
	public void testSlider() {
		captchaProperties.setType(CaptchaType.Slider);
		SliderCaptcha sliderCaptcha = new SliderCaptcha(captchaProperties);
		CaptchaCode captchaCode = sliderCaptcha.out();
		log.info(captchaCode.getText());
	}

	@Test
	public void testClickWord() {
		CaptchaProperties.ClickWord clickWord = new CaptchaProperties.ClickWord();
		clickWord.setClickCount(6);
		clickWord.setFontColorRandom(true);
		captchaProperties.setClickWord(clickWord);
		captchaProperties.setType(CaptchaType.ClickWord);
		ClickWordCaptcha clickWordCaptcha = new ClickWordCaptcha(captchaProperties);
		CaptchaCode captchaCode = clickWordCaptcha.out();
		log.info(captchaCode.getText());
		log.info(JSON.toJSONString(captchaCode.getPoints()));
		FileUtil.writeBytes(Base64.decode(captchaCode.getSrcImg()), "G:/a/1.jpg");
	}
	
	
	
	
	

}
