package cn.darkjrong.xxljob.service;

import cn.darkjrong.xxljob.domain.XxlJobInfo;

import java.util.List;

/**
 * xxl-job 信息服务类
 *
 * @author Rong.Jia
 * @date 2023/01/11
 */
public interface XxlJobInfoService {

    /**
     * 获取任务信息
     *
     * @param jobGroupId      任务组ID
     * @param executorHandler 遗嘱执行人处理程序
     * @return {@link List}<{@link XxlJobInfo}>
     */
    List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler);

    /**
     * 添加任务信息
     *
     * @param xxlJobInfo xxl任务信息
     * @return {@link Boolean }
     */
    Boolean addJobInfo(XxlJobInfo xxlJobInfo);

    /**
     * 修改任务信息
     *
     * @param xxlJobInfo xxl任务信息
     * @return {@link Boolean }
     */
    Boolean updateJobInfo(XxlJobInfo xxlJobInfo);

}
