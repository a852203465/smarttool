package cn.darkjrong.retry.handler;

/**
 * 定义有异常函数式接口
 *
 * @author Rong.Jia
 * @date 2025/09/27
 */
@FunctionalInterface
public interface FunctionHandlerThrow<T> {
    T apply() throws Exception;
}

