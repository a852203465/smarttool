package cn.darkjrong.captcha.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 算术类型
 *
 * @author Rong.Jia
 * @date 2025/10/06
 */
@Getter
@AllArgsConstructor
public enum ArithmeticType {

    /**
     * ADD:加法
     * ADD_SUB:加减法
     * ADD_SUB_MUL:加减乘法
     * ADD_SUB_MUL_DIV:加减乘除法
     *
     */

    ADD(2),
    ADD_SUB(3),
    ADD_SUB_MUL(4),
    ADD_SUB_MUL_DIV(5),





    ;

    private final Integer value;





}
