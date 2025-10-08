# 验证码模块
Java图形验证码，支持gif、中文、算术、滑块等类型，可用于Java Web、JavaSE等项目。

## 1.效果展示

**普通类型：**

![验证码](https://s2.ax1x.com/2019/08/23/msFrE8.png) 
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msF0DP.png)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msFwut.png)

**Gif类型：**

![验证码](https://s2.ax1x.com/2019/08/23/msFzVK.gif) 
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msFvb6.gif)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msFXK1.gif)

**算术类型：**

![验证码](https://s2.ax1x.com/2019/08/23/mskKPg.png)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msknIS.png)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/mskma8.png)

**中文类型：**

![验证码](https://s2.ax1x.com/2019/08/23/mskcdK.png)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msk6Z6.png)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msksqx.png)

**内置字体：**

![验证码](https://s2.ax1x.com/2019/08/23/msAVSJ.png)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msAAW4.png)
&emsp;&emsp;
![验证码](https://s2.ax1x.com/2019/08/23/msAkYF.png)

## 2.使用方法
### 2.1引入依赖
```xml
<dependencies>
    <dependency>
        <groupId>cn.darkjrong</groupId>
        <artifactId>smarttool-captcha</artifactId>
        <version>${latestversion}</version>
    </dependency>
</dependencies>
```
### 2.2 Maven工程环境
```java
@Slf4j
public class CaptchaTest {

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
```
### 2.3 Spring Boot环境
#### 2.3.1 配置参数(application.properties) yml配置
```yaml
stl:
  captcha:
    # 验证码类型
    type: arithmetic
    # 验证码图片高
    height: 100
    # 验证码图片宽
    width: 300
    # 验证码位数
    length: 3
    # 过期时长
    exp-duration: 5m
    # 文本组合类型
    text-type: type_num_and_upper
    font:
      # 字体类型
      font-type: action_jackson
      # 字体大小
      size: 32
    arithmetic:
      # 算术算法
      algorithm: add_sub_mul_div
      # 难度,默认：10
      difficulty: 10
    click-word:
      click-count: 4
      font-color-random: true
```

#### 2.3.2 API调用
```java
@Autowired
private CaptchaTemplate captchaTemplate;
```

## 3.更多设置
### 3.1验证码类型
> 注意：<br/>
> &emsp;算术验证码的len表示是几位数运算，而其他验证码的len表示验证码的位数，算术验证码的text()表示的是公式的结果，
> 对于算术验证码，你应该把公式的结果存储session，而不是公式。

### 3.2验证码字符类型
 类型 | 描述 
 :--- | :--- 
 TYPE_DEFAULT | 数字和字母混合 
 TYPE_ONLY_NUMBER | 纯数字
 TYPE_ONLY_CHAR | 纯字母 
 TYPE_ONLY_UPPER | 纯大写字母
 TYPE_ONLY_LOWER | 纯小写字母
 TYPE_NUM_AND_UPPER | 数字和大写字母

> 只有`SpecCaptcha`和`GifCaptcha`设置才有效果。

### 3.3字体设置
 字体 | 效果 
 :--- | :--- 
 Captcha.FONT_1 |  ![](https://s2.ax1x.com/2019/08/23/msMe6U.png)
 Captcha.FONT_2 | ![](https://s2.ax1x.com/2019/08/23/msMAf0.png)
 Captcha.FONT_3 |  ![](https://s2.ax1x.com/2019/08/23/msMCwj.png)
 Captcha.FONT_4 | ![](https://s2.ax1x.com/2019/08/23/msM9mQ.png)
 Captcha.FONT_5 | ![](https://s2.ax1x.com/2019/08/23/msKz6S.png)
 Captcha.FONT_6 | ![](https://s2.ax1x.com/2019/08/23/msKxl8.png)
 Captcha.FONT_7 | ![](https://s2.ax1x.com/2019/08/23/msMPTs.png)
 Captcha.FONT_8 | ![](https://s2.ax1x.com/2019/08/23/msMmXF.png)
 Captcha.FONT_9 | ![](https://s2.ax1x.com/2019/08/23/msMVpV.png)
 Captcha.FONT_10 | ![](https://s2.ax1x.com/2019/08/23/msMZlT.png)

## 4.自定义实现验证码存储(Redis)
```java
@Slf4j
@Component
public class RedisCaptchaStore extends AbstractCaptchaStore {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void store(String captchaId, String value, Duration expDuration) {
        redisUtils.setEx(captchaId, value, expDuration.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void remove(String captchaId) {
        redisUtils.delete(captchaId);
    }

    @Override
    public String get(String captchaId) {
        return Convert.toStr(redisUtils.get(captchaId));
    }

}
```
## 5.自定义效果
中文验证码可继承`AbstractChineseCaptcha`，算术验证码可继承`AbstractArithmeticCaptcha`。
