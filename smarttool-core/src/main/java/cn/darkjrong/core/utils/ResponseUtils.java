package cn.darkjrong.core.utils;

import cn.darkjrong.core.exceptions.StlWebException;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import cn.darkjrong.core.domain.ResponseVO;
import cn.darkjrong.core.enums.ErrorEnum;
import cn.darkjrong.core.enums.ExceptionEnum;
import cn.darkjrong.core.lang.constants.NumberConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应工具类
 *
 * @author Rong.Jia
 * @date 2021/08/03 17:36:07
 */
@Slf4j
public class ResponseUtils {

    /**
     * 获取响应
     *
     * @param response 响应
     * @param clazz    clazz 类型
     * @return {@link T}
     */
    public static <T> T getResponse(ResponseVO<T> response, Class<T> clazz) {
        if (Validator.equal(NumberConstant.ZERO, response.getCode())) {
            T data = response.getData();
            if (ObjectUtil.isNotNull(data)) {
                return JSON.parseObject(JSON.toJSONString(data), clazz);
            }
            return data;
        }
        return null;
    }

    /**
     * 获取响应
     *
     * @param response      响应
     * @param typeReference 引用类型
     * @return {@link T}
     */
    public static <T> T getResponse(ResponseVO<T> response, TypeReference<T> typeReference) {
        if (Validator.equal(NumberConstant.ZERO, response.getCode())) {
            T data = response.getData();
            if (ObjectUtil.isNotNull(data)) {
                return JSON.parseObject(JSON.toJSONString(data), typeReference);
            }
            return data;
        }
        return null;
    }

    /**
     * 响应成功
     *
     * @param response     响应
     * @param exceptionEnum 响应枚举
     */
    public static void success(ResponseVO<?> response, ExceptionEnum exceptionEnum) {
        if (!Validator.equal(NumberConstant.ZERO, response.getCode())) {
            log.error("success {}", exceptionEnum.getMessage());
            throw new StlWebException(exceptionEnum);
        }
    }

    /**
     * 响应成功
     *
     * @param response 响应
     * @param code     代码
     * @param message  消息
     */
    public static void success(ResponseVO<?> response, Integer code, String message) {
        if (!Validator.equal(NumberConstant.ZERO, response.getCode())) {
            log.error("success {}", message);
            throw new StlWebException(code, message);
        }
    }

    /**
     * 响应成功
     *
     * @param response 响应
     */
    public static void success(ResponseVO<?> response) {
        if (!Validator.equal(NumberConstant.ZERO, response.getCode())) {
            log.error("success {}", response.getMessage());
            throw new StlWebException(ErrorEnum.ERROR.getCode(), response.getMessage());
        }
    }

    /**
     * 响应成功
     *
     * @param response 响应
     * @return {@link Boolean}
     */
    public static Boolean isSuccess(ResponseVO<?> response) {
       return Validator.equal(ErrorEnum.SUCCESS.getCode(), response.getCode());
    }

    /**
     * 获取响应
     *
     * @param response 响应
     * @return {@link T} 响应信息
     */
    public static <T> T getResponse(ResponseVO<T> response) {
        if (Validator.equal(NumberConstant.ZERO, response.getCode())) {
            return response.getData();
        }
        return null;
    }

    /**
     * 获取响应
     *
     * @param response 响应
     * @param exceptionEnum 异常枚举
     * @return {@link T} 响应信息
     * @throws StlWebException 异常
     */
    public static <T> T getResponse(ResponseVO<T> response, ExceptionEnum exceptionEnum) {
        if (Validator.equal(NumberConstant.ZERO, response.getCode())) {
            return response.getData();
        }
        log.error("getResponse {}", exceptionEnum.getMessage());
        throw new StlWebException(exceptionEnum);
    }

    /**
     * 获取响应, 不成功则抛出异常
     *
     * @param response 响应
     * @return {@link T} 响应信息
     * @throws StlWebException 异常
     */
    public static <T> T getResponseException(ResponseVO<T> response) {
        if (Validator.equal(NumberConstant.ZERO, response.getCode())) {
            return response.getData();
        }
        String message = StrUtil.isBlank(response.getMessage()) ? ErrorEnum.SYSTEM_ERROR.getMessage() : response.getMessage();
        throw new StlWebException(ErrorEnum.ERROR.getCode(), message);
    }












}
