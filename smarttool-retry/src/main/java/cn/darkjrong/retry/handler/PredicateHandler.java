package cn.darkjrong.retry.handler;

/**
 * 定义有断言类函数式接口
 *
 * @author Rong.Jia
 * @date 2025/09/27
 */
@FunctionalInterface
public interface PredicateHandler {
    void test();
}
