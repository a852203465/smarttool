package cn.darkjrong.captcha;

import cn.darkjrong.captcha.domain.CaptchaCode;
import cn.darkjrong.captcha.enums.ArithmeticType;
import cn.darkjrong.captcha.enums.FontType;
import cn.darkjrong.captcha.factory.cap.*;
import cn.darkjrong.captcha.uitls.FontUtils;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
	public void testMath() throws FileNotFoundException {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(captchaProperties);
		CaptchaCode captchaCode = captcha.out(new FileOutputStream(getPath("math.png")));
		log.info("text {}", captchaCode.getText());
	}
	@Test
	public void test() throws Exception {
		for (int i = 0; i < 1; i++) {
			CaptchaProperties.FontProperties font = new CaptchaProperties.FontProperties();
			font.setFontType(FontType.Action_Jackson);
			font.setSize(32f);
			captchaProperties.setFont(font);
			SpecCaptcha specCaptcha = new SpecCaptcha(captchaProperties);
			CaptchaCode captchaCode = specCaptcha.out(new FileOutputStream(getPath(+i + "1.png")));
			log.info(captchaCode.getText());
		}
	}

	@Test
	public void testGIf() throws Exception {
		for (int i = 0; i < 1; i++) {
			CaptchaProperties.FontProperties font = new CaptchaProperties.FontProperties();
			captchaProperties.setLength(5);
			font.setFontType(FontType.Action_Jackson);
			font.setSize(32f);
			captchaProperties.setFont(font);
			GifCaptcha gifCaptcha = new GifCaptcha(captchaProperties);
			CaptchaCode captchaCode = gifCaptcha.out(new FileOutputStream(getPath(+i + "2.gif")));
			log.info(captchaCode.getText());
		}
	}

	@Test
	public void testHan() throws Exception {
		for (int i = 0; i < 1; i++) {
			ChineseCaptcha chineseCaptcha = new ChineseCaptcha(captchaProperties);
			CaptchaCode captchaCode = chineseCaptcha.out(new FileOutputStream(getPath(+i + "3.png")));
			log.info(captchaCode.getText());
		}
	}

	@Test
	public void testGifHan() throws Exception {
		for (int i = 0; i < 1; i++) {
			ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha(captchaProperties);
			CaptchaCode captchaCode = chineseGifCaptcha.out(new FileOutputStream(getPath(+i + "4.gif")));
			log.info(captchaCode.getText());
		}
	}

	@Test
	public void testArit() throws Exception {
		for (int i = 0; i < 1; i++) {
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
			CaptchaCode captchaCode = specCaptcha.out(new FileOutputStream(getPath(+i + "5.png")));
			log.info(specCaptcha.getCalculationFormula() + " " + captchaCode.getText());
		}
	}


	private static String getPath(String name) {
		return "G:/a/" + name;
	}

}
