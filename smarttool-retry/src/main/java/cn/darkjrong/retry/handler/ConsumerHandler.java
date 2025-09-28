package cn.darkjrong.retry.handler;

/**
 * 定义有输入无输出类函数式接口
 *
 * @author Rong.Jia
 * @date 2025/09/27
 */
@FunctionalInterface
public interface ConsumerHandler {
    void accept();
}
