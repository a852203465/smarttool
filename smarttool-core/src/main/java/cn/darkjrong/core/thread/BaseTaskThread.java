package cn.darkjrong.core.thread;

import cn.hutool.core.thread.ThreadUtil;

/**
 * 公共任务线程
 *
 * @author Rong.Jia
 * @date 2023/05/18
 */
public abstract class BaseTaskThread extends AbstractThreadListener {

    /**
     * 创建线程
     *
     * @param runnable   {@link Runnable}
     * @param threadName 线程名字
     * @return {@link Thread}
     */
    public Thread newThread(Runnable runnable, String threadName) {
        Thread thread = ThreadUtil.newThread(runnable, threadName, Boolean.TRUE);
        thread.start();
        return thread;
    }




}
