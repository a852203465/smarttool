package cn.darkjrong.core.enums;

/**
 * 异常顶级接口，所有枚举均需实现该类
 *
 * @author Rong.Jia
 * @date 2023/06/27
 */
public interface ExceptionEnum {

    /**
     * 获取错误码
     *
     * @return {@link Integer}
     */
    Integer getCode();

    /**
     * 获取错误消息
     *
     * @return {@link String}
     */
    String getMessage();

}
