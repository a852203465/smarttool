package cn.darkjrong.captcha.factory.cap;

import cn.darkjrong.captcha.enums.Symbol;
import cn.darkjrong.captcha.uitls.Randoms;
import cn.darkjrong.spring.boot.autoconfigure.CaptchaProperties;
import com.googlecode.aviator.AviatorEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * 算术验证码抽象类
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
public abstract class AbstractArithmeticCaptcha extends AbstractCaptcha {

    /**
     * 计算公式
     */
    private String calculationFormula;

    public AbstractArithmeticCaptcha(CaptchaProperties captchaProperties) {
        super(captchaProperties);
    }

    @Override
    protected void alphas() {
        List<String> arithmeticList = new ArrayList<>(len + len - 1);
        Symbol lastSymbol = null;
        int divAmount = 0;
        for (int i = 0; i < len; i++) {
            int number = Randoms.num(difficulty);
            // 如果上一步生成的为除号，要重新设置除数和被除数，确保难度满足设定要求且可以整除
            if (lastSymbol == Symbol.DIV) {
                number = (int) Math.sqrt(number);
                // 避免被除数为 0
                number = number == 0 ? 1 : number;
                arithmeticList.set(2 * (i - 1), String.valueOf(number * Randoms.num((int) Math.sqrt(difficulty))));
            }

            // 如果是减法则获取一个比第一个小的数据
            if (lastSymbol == Symbol.SUB) {
                String firstNum = arithmeticList.get(0);
                number = Randoms.num(Integer.parseInt(firstNum) + 1);
            }
            arithmeticList.add(String.valueOf(number));
            if (i < len - 1) {
                int type;

                // 除法只出现一次，否则还需要递归更新除数，第一个除数将会很大
                if (divAmount == 1) {
                    type = Randoms.num(1, arithmeticType.getValue() - 1);
                } else {
                    type = Randoms.num(1, arithmeticType.getValue());
                }

                if (type == 1) {
                    arithmeticList.add((lastSymbol = Symbol.ADD).getValue());
                } else if (type == 2) {
                    arithmeticList.add((lastSymbol = Symbol.SUB).getValue());
                } else if (type == 3) {
                    arithmeticList.add((lastSymbol = Symbol.MUL).getValue());
                } else if (type == 4) {
                    arithmeticList.add((lastSymbol = Symbol.DIV).getValue());
                    divAmount++;
                }
            }
        }
        calculationFormula = String.join("", arithmeticList);
        calculationFormula = calculationFormula.replace("x", "*").replace("÷", "/");
        chars = String.valueOf(AviatorEvaluator.execute(calculationFormula));
        calculationFormula += " = ?";
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

    public String getCalculationFormula() {
        return calculationFormula;
    }
}
