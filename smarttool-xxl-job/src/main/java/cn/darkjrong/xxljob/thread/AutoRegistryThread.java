package cn.darkjrong.xxljob.thread;

import cn.darkjrong.core.thread.BaseTaskThread;
import cn.darkjrong.spring.boot.autoconfigure.XxlJobProperties;
import cn.darkjrong.xxljob.annotation.XxlJobRegister;
import cn.darkjrong.xxljob.domain.XxlJobGroup;
import cn.darkjrong.xxljob.domain.XxlJobInfo;
import cn.darkjrong.xxljob.exceptions.XxlJobException;
import cn.darkjrong.xxljob.service.XxlJobGroupService;
import cn.darkjrong.xxljob.service.XxlJobInfoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自动注册线程
 *
 * @author Rong.Jia
 * @date 2025/10/04
 */
@Slf4j
@Component
public class AutoRegistryThread extends BaseTaskThread {

    @Autowired
    private XxlJobGroupService jobGroupService;

    @Autowired
    private XxlJobInfoService jobInfoService;

    @Autowired
    private XxlJobProperties xxlJobProperties;

    @Autowired
    private ApplicationContext applicationContext;

    private Thread registryThread;
    private volatile boolean toStop = false;

    @Override
    public void start() {
        if (!xxlJobProperties.isAutoRegister()) {
            log.warn(">>>>>>>>>>> xxl-job, Automatic registration is not enabled, please check xxl-job configuration.");
            return;
        }

        registryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!toStop) {
                    try {
                        addJobGroup();
                        addJobInfo();
                        stop();
                    } catch (Throwable e) {
                        if (!toStop) {
                            log.error(">>>>>>>>>>> xxl-job, auto registry thread exception, error msg 【{}】", e.getMessage());
                        }
                    }

                    try {
                        if (!toStop) {
                            TimeUnit.SECONDS.sleep(5);
                        }
                    } catch (Throwable e) {
                        if (!toStop) {
                            log.warn(">>>>>>>>>>> xxl-job, auto registry thread interrupted, error msg 【{}】", e.getMessage());
                        }
                    }
                }
                log.info(">>>>>>>>>>> xxl-job, auto registry thread destroy.");
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("xxl-job, AutoRegistryThread");
        registryThread.start();
    }

    @Override
    public void stop() {
        toStop = true;
        ThreadUtil.interrupt(registryThread, Boolean.TRUE);
    }

    private void addJobGroup() {
        if (jobGroupService.preciselyCheck()) {
            return;
        }

        if(jobGroupService.registerGroup()) {
            log.info("appName【{}】register xxl-job group success!", xxlJobProperties.getExecutor().getAppName());
        }
    }

    private void addJobInfo() {
        String appName = xxlJobProperties.getExecutor().getAppName();
        XxlJobGroup xxlJobGroup = jobGroupService.getJobGroup(appName);
        if (ObjectUtil.isNull(xxlJobGroup)) {
            if (!jobGroupService.registerGroup()) {
                log.error("appName【{}】register xxl-job group error!", appName);
                throw new XxlJobException("appName【{}】register xxl-job group error!", appName);
            }
            xxlJobGroup = jobGroupService.getJobGroup(appName);
        }

        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, XxlJob> annotatedMethods  = MethodIntrospector.selectMethods(bean.getClass(),
                    (MethodIntrospector.MetadataLookup<XxlJob>) method -> AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class));

            for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                XxlJob xxlJob = methodXxlJobEntry.getValue();

                //自动注册
                if (executeMethod.isAnnotationPresent(XxlJobRegister.class)) {
                    XxlJobRegister xxlRegister = executeMethod.getAnnotation(XxlJobRegister.class);
                    XxlJobInfo xxlJobInfo = createXxlJobInfo(xxlJobGroup, xxlJob, xxlRegister);

                    List<XxlJobInfo> jobInfo = jobInfoService.getJobInfo(xxlJobGroup.getId(), xxlJob.value());
                    if (CollUtil.isNotEmpty(jobInfo)) {
                        //因为是模糊查询，需要再判断一次
                        XxlJobInfo first = jobInfo.stream()
                                .filter(a -> a.getExecutorHandler().equals(xxlJob.value()))
                                .findFirst().orElse(null);
                        if (ObjectUtil.isNotNull(first)) {
                            xxlJobInfo.setId(first.getId());
                            if (jobInfoService.updateJobInfo(xxlJobInfo)) {
                                log.info("job-group【{}】job-info【{}】Change register xxl-job success!", xxlJobGroup.getAppname(), xxlJob.value());
                            }
                        }
                    } else {
                        if (jobInfoService.addJobInfo(xxlJobInfo)) {
                            log.info("job-group【{}】job-info【{}】register xxl-job success!", xxlJobGroup.getAppname(), xxlJobInfo.getExecutorHandler());
                        }
                    }
                }
            }
        }
    }

    private XxlJobInfo createXxlJobInfo(XxlJobGroup xxlJobGroup, XxlJob xxlJob, XxlJobRegister xxlRegister){
        XxlJobInfo xxlJobInfo=new XxlJobInfo();
        xxlJobInfo.setJobGroup(xxlJobGroup.getId());
        xxlJobInfo.setJobDesc(xxlRegister.description());
        xxlJobInfo.setAuthor(xxlRegister.author());
        xxlJobInfo.setScheduleType(xxlRegister.scheduleType().name());
        xxlJobInfo.setScheduleConf(xxlRegister.scheduleConf());
        xxlJobInfo.setGlueType(xxlRegister.glueType().name());
        xxlJobInfo.setExecutorHandler(xxlJob.value());
        xxlJobInfo.setAlarmEmail(xxlRegister.alarmEmail());
        xxlJobInfo.setExecutorRouteStrategy(xxlRegister.routeStrategy().name());
        xxlJobInfo.setMisfireStrategy(xxlRegister.expireStrategy().name());
        xxlJobInfo.setExecutorBlockStrategy(xxlRegister.blockStrategy().name());
        xxlJobInfo.setExecutorTimeout(xxlRegister.timeout());
        xxlJobInfo.setExecutorFailRetryCount(xxlRegister.failRetryCount());
        xxlJobInfo.setGlueRemark("GLUE代码初始化");
        xxlJobInfo.setTriggerStatus(xxlRegister.triggerStatus());

        return xxlJobInfo;
    }




}
