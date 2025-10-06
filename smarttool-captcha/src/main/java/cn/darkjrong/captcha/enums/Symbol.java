package cn.darkjrong.captcha.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 标识符
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
@Getter
@RequiredArgsConstructor
public enum Symbol {

	/**
	 * 标识符
	 */
	NUM("n", false),

	/**
	 * 加法
	 */
	ADD("+", false),

	/**
	 * 减发
	 */
	SUB("-", false),

	/**
	 * 乘法
	 */
	MUL("x", true),

	/**
	 * 除法
	 */
	DIV("÷", true);

	/**
	 * 算数符号
	 */
	private final String value;

	/**
	 * 是否优先计算
	 */
	private final boolean priority;

	public static Symbol of(String c) {
		Symbol[] values = Symbol.values();
		for (Symbol value : values) {
			if (value.value.equals(c)) {
				return value;
			}
		}
		throw new IllegalArgumentException("不支持的标识符，仅仅支持(+、-、×、÷)");
	}

}
