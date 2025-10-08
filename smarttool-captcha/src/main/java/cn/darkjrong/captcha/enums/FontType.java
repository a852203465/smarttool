package cn.darkjrong.captcha.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 字体类型
 *
 * @author Rong.Jia
 * @date 2025/10/05
 */
@Getter
@AllArgsConstructor
public enum FontType {


    Action_Jackson("Action Jackson"),
    Epilog("Epilog"),
    Fresnel("Fresnel"),
    Toms_Headache("Tom's Headache"),
    Lexographer("Lexographer"),
    Prefix("Prefix"),
    PROG_BOT("PROG.BOT"),
    Ransom("Ransom"),
    Robot_Teacher("Robot Teacher"),
    Potassium_Scandal("Potassium Scandal"),
    Arial("楷体"),
    WenQuanZhengHei("文泉驿正黑"),




    ;

    private final String value;


    public static FontType of(String value) {
        return Arrays.stream(FontType.values())
                .filter(a -> a.getValue().equals(value))
                .findAny().orElse(Arial);
    }


}
