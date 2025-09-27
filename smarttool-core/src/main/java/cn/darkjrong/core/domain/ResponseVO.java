package cn.darkjrong.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import cn.darkjrong.core.enums.ErrorEnum;
import cn.darkjrong.core.enums.ExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据格式返回统一
 *
 * @author Rong.Jia
 * @date 2023/06/27
 */
@Data
@ApiModel("返回对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = 3681838956784534606L;

    /**
     * 异常码
     */
    @ApiModelProperty("异常码")
    private Integer code;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String message;

    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private T data;

    public ResponseVO() {}

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseVO(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseVO(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public ResponseVO(ExceptionEnum exceptionEnum, T data) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
        this.data = data;
    }

    public static <T> ResponseVO<T> success(){
        return new ResponseVO<>(ErrorEnum.SUCCESS);
    }

    public static <T> ResponseVO<T> success(T data){
        return new ResponseVO<>(ErrorEnum.SUCCESS, data);
    }

    public static <T> ResponseVO<T> success(int code, String msg){
        return new ResponseVO<>(code, msg);
    }

    public static <T> ResponseVO<T> error(int code, String msg){
        return new ResponseVO<>(code, msg);
    }

    public static <T> ResponseVO<T> error(ExceptionEnum exceptionEnum){
        return new ResponseVO<>(exceptionEnum);
    }

    public static ResponseVO<?> error(ExceptionEnum exceptionEnum, Object data){
        return new ResponseVO<>(exceptionEnum, data);
    }

    public static <T> ResponseVO<T> error(){
        return new ResponseVO<>(ErrorEnum.ERROR);
    }

    public static <T> ResponseVO<T> error(T data){
        return new ResponseVO<>(ErrorEnum.ERROR, data);
    }

    public static <T> ResponseVO<T> error(String message){
        return new ResponseVO<>(ErrorEnum.ERROR.getCode(), message);
    }


}
