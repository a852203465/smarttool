package cn.darkjrong.xxljob.service.impl;

import cn.darkjrong.spring.boot.autoconfigure.XxlJobProperties;
import cn.darkjrong.xxljob.domain.XxlJobGroup;
import cn.darkjrong.xxljob.enums.UrlEnum;
import cn.darkjrong.xxljob.exceptions.XxlJobException;
import cn.darkjrong.xxljob.service.XxlJobGroupService;
import cn.darkjrong.xxljob.service.XxlJobLoginService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.zhxu.okhttps.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * xxl-job组服务类实现类
 *
 * @author Rong.Jia
 * @date 2023/01/11
 */
@Slf4j
@Service
public class XxlJobGroupServiceImpl implements XxlJobGroupService {

    @Autowired
    private XxlJobLoginService xxlJobLoginService;

    @Autowired
    private XxlJobProperties xxlJobProperties;

    @Override
    public List<XxlJobGroup> getJobGroup() {
        String url = xxlJobProperties.getAdmin().getAddresses() + UrlEnum.JOB_GROUP_PAGE_LIST.getValue();
        String appName = xxlJobProperties.getExecutor().getAppName();
        String name = StrUtil.isEmpty(xxlJobProperties.getExecutor().getName())
                ? appName
                : xxlJobProperties.getExecutor().getName();
        Map<String, Object> params = MapUtil.newHashMap();
        params.put("title", name);
        params.put("appname", appName);

        String cookie = xxlJobLoginService.getCookie();
        String res = HttpUtils.sync(url)
                .addUrlPara(params)
                .addHeader("Cookie", cookie)
                .get()
                .getBody()
                .toString();
        return JSONObject.parseObject(res).getObject("data", new TypeReference<List<XxlJobGroup>>() {});
    }

    @Override
    public XxlJobGroup getJobGroup(String appName) {
        List<XxlJobGroup> xxlJobGroups = getJobGroup();
        if (CollectionUtil.isNotEmpty(xxlJobGroups)) {
            return xxlJobGroups.stream()
                    .filter(a -> StrUtil.equals(appName, a.getAppname()))
                    .findAny()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public boolean registerGroup() {
        if (xxlJobProperties.getExecutor().getAddressType().equals(1)
                && CollectionUtil.isEmpty(xxlJobProperties.getExecutor().getAddresses())){
            log.error("手动录入模式下,执行器地址列表不能为空");
            throw new XxlJobException("手动录入模式下,执行器地址列表不能为空");
        }

        String url = xxlJobProperties.getAdmin().getAddresses() + UrlEnum.JOB_GROUP_SAVE.getValue();
        XxlJobGroup xxlJobGroup = getJobGroup(xxlJobProperties.getExecutor().getAppName());
        if (ObjectUtil.isNotNull(xxlJobGroup)) {
            url = xxlJobProperties.getAdmin().getAddresses() + UrlEnum.JOB_GROUP_UPDATE.getValue();
        }

        String name = StrUtil.isEmpty(xxlJobProperties.getExecutor().getName())
                ? xxlJobProperties.getExecutor().getAppName()
                :xxlJobProperties.getExecutor().getName();

        String cookie = xxlJobLoginService.getCookie();

        Map<String, Object> params = MapUtil.newHashMap();
        params.put("title", name);
        params.put("appname", xxlJobProperties.getExecutor().getAppName());
        params.put("addressType", xxlJobProperties.getExecutor().getAddressType());
        params.put("addressList", CollectionUtil.join(xxlJobProperties.getExecutor().getAddresses(), StrUtil.COMMA));
        if (ObjectUtil.isNotNull(xxlJobGroup)) {
            params.put("id", xxlJobGroup.getId());
        }

        String res = HttpUtils.sync(url)
                .addBodyPara(params)
                .addHeader("Cookie", cookie)
                .post()
                .getBody()
                .toString();
        return JSON.parseObject(res).getInteger("code").equals(200);
    }

    @Override
    public boolean preciselyCheck() {
        List<XxlJobGroup> jobGroup = getJobGroup();
        Optional<XxlJobGroup> has = jobGroup.stream()
                .filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(xxlJobProperties.getExecutor().getAppName())
                        && xxlJobGroup.getTitle().equals(xxlJobProperties.getExecutor().getName()))
                .findAny();
        return has.isPresent();
    }

}
