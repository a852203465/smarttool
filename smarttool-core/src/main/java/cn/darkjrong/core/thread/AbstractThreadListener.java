package cn.darkjrong.core.thread;

import cn.darkjrong.core.enums.ThreadStatus;
import cn.darkjrong.core.events.ThreadEvent;
import org.springframework.context.ApplicationListener;

/**
 * 线程抽象类
 *
 * @author Rong.Jia
 * @date 2023/05/15
 */
public abstract class AbstractThreadListener implements ApplicationListener<ThreadEvent> {

    @Override
    public void onApplicationEvent(ThreadEvent event) {
        if (ThreadStatus.START.equals(event.getThreadStatus())) {
            this.start();
        }else if (ThreadStatus.STOP.equals(event.getThreadStatus())) {
            this.stop();
        }
    }

    /**
     * 开始
     */
    public abstract void start();

    /**
     * 停止
     */
    public abstract void stop();







}
