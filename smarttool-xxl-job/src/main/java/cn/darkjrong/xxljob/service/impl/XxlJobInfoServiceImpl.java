package cn.darkjrong.xxljob.service.impl;

import cn.darkjrong.spring.boot.autoconfigure.XxlJobProperties;
import cn.darkjrong.xxljob.domain.XxlJobInfo;
import cn.darkjrong.xxljob.enums.UrlEnum;
import cn.darkjrong.xxljob.service.XxlJobInfoService;
import cn.darkjrong.xxljob.service.XxlJobLoginService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.zhxu.okhttps.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * xxl-job工作信息服务类实现类
 *
 * @author Rong.Jia
 * @date 2023/01/11
 */
@Slf4j
@Service
public class XxlJobInfoServiceImpl implements XxlJobInfoService {

    @Autowired
    private XxlJobLoginService xxlJobLoginService;

    @Autowired
    private XxlJobProperties xxlJobProperties;

    @Override
    public List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler) {
        String url = xxlJobProperties.getAdmin().getAddresses() + UrlEnum.JOB_PAGE_LIST.getValue();

        String cookie = xxlJobLoginService.getCookie();

        Map<String, Object> params = MapUtil.newHashMap();
        params.put("jobGroup", jobGroupId);
        params.put("executorHandler", executorHandler);
        params.put("triggerStatus", -1);

        String res = HttpUtils.sync(url)
                .addUrlPara(params)
                .addHeader("Cookie", cookie)
                .get()
                .getBody()
                .toString();
        return JSON.parseObject(res).getObject("data", new TypeReference<List<XxlJobInfo>>() {});
    }

    @Override
    public Boolean addJobInfo(XxlJobInfo xxlJobInfo) {
        String url = xxlJobProperties.getAdmin().getAddresses() + UrlEnum.JOB_SAVE.getValue();
        Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);

        String cookie = xxlJobLoginService.getCookie();
        String res = HttpUtils.sync(url)
                .addBodyPara(paramMap)
                .addHeader("Cookie", cookie)
                .post()
                .getBody()
                .toString();
        return JSON.parseObject(res).getInteger("code").equals(200);
    }

    @Override
    public Boolean updateJobInfo(XxlJobInfo xxlJobInfo) {
        String url = xxlJobProperties.getAdmin().getAddresses() + UrlEnum.JOB_UPDATE.getValue();
        Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);

        String cookie = xxlJobLoginService.getCookie();
        String res = HttpUtils.sync(url)
                .addBodyPara(paramMap)
                .addHeader("Cookie", cookie)
                .post()
                .getBody()
                .toString();
        return JSON.parseObject(res).getInteger("code").equals(200);
    }

}
