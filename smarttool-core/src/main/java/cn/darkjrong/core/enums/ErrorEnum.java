package cn.darkjrong.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误枚举
 *
 * @author Rong.Jia
 * @date 2023/06/27
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum implements ExceptionEnum {

    /**
     *  枚举类code 开头使用规则：
     *  0: 成功；
     *  500: 失败；
     *  400：参数不正确
     *  401： 登录相关  需跳登录
     *  404：未找到
     *  405：请求方式错误
     *  415：媒体类型不支持
     */

    // 成功
    SUCCESS(0,"成功"),

    // 参数不正确
    PARAMETER_ERROR(400, "参数不正确"),

    // 失败
    ERROR(500, "失败"),
    SYSTEM_ERROR(500, "系统错误"),
    FILE_LIMIT_EXCEEDED(500, "文件超出限制, 请选择较小文件"),

    // 未找到
    NOT_FOUND(404, "请求接口不存在"),

    // 请求方式错误
    REQUEST_MODE_ERROR(405, "请求方式错误, 请检查"),

    //媒体类型不支持
    MEDIA_TYPE_NOT_SUPPORTED(415, "媒体类型不支持"),

    REQUEST_PARAMETER_FORMAT_IS_INCORRECT(400, "请求参数格式不正确"),
    THE_PARAMETER_TYPE_IS_INCORRECT(400, "参数类型不正确"),
    LACK_OF_PARAMETER(400, "缺少必要参数，请检查"),


    BUCKET_NAME_CANNOT_BE_NULL(10000,"bucket不能为空,请检查"),
    FILE_DOES_NOT_EXIST(10000,"文件不能为空,请检查"),
    BYTES_CANNOT_BE_EMPTY(10000,"文件字节不能为空,请检查"),
    DIRECTORY_CANNOT_BE_EMPTY(10000,"目录不能为空,请检查"),
    THE_INPUT_STREAM_CANNOT_BE_EMPTY(10000,"输入流不能为空,请检查"),
    FILE_NAME_CANNOT_BE_EMPTY(10000,"文件名不能为空,请检查"),
    THE_FILE_TO_BE_DELETED_CANNOT_BE_EMPTY(10000,"待删除文件列表不能为空,请检查"),
    FILE_OBJECT_ACQUISITION_ACCESSIBLE_CONNECTION_EXCEPTION(10000, "文件对象获取可访问连接异常"),

    // 附件不存在
    ATTACHMENT_DOES_NOT_EXIST(10000, "附件不存在"),

    // 接收者不能为空
    THE_RECEIVER_CANNOT_BE_EMPTY(10000, "接收者不能为空"),

    // 消息不能为空
    THE_MESSAGE_CANNOT_BE_EMPTY(10000, "消息不能为空"),

    // 你的邮件客户端不支持html邮件
    YOUR_EMAIL_CLIENT_DOES_NOT_SUPPORT_HTML_MESSAGES(10000, "你的邮件客户端不支持html邮件"),

    // 主题不能为空
    THE_TOPIC_CANNOT_BE_EMPTY(10000, "主题不能为空"),

    // 属性不能为空
    THE_PROPERTY_CANNOT_BE_EMPTY(10000, "'%s' 属性不能为空"),

    // 邮箱账号信息不能为空
    THE_EMAIL_ACCOUNT_INFORMATION_CANNOT_BE_EMPTY(10000, "邮箱账号信息不能为空"),

    CAN_NOT_RECIPIENT_BCC_CC_AT_THE_SAME_TIME_FOR_EMPTY(10000, "不能收件人,密送人,抄送人同时为空"),

    BUCKET_NAME_CANNOT_BE_EMPTY(10000, "Bucket cannot be empty"),

    OBJECT_NAME_CANNOT_BE_EMPTY(10000, "object cannot be empty"),
    SOURCE_BUCKET_CANNOT_BE_EMPTY(10000, "source bucket cannot be empty"),
    TARGET_BUCKET_CANNOT_BE_EMPTY(10000, "target bucket cannot be empty"),
    SOURCE_OBJECT_CANNOT_BE_EMPTY(10000, "source object cannot be empty"),
    TARGET_OBJECT_CANNOT_BE_EMPTY(10000, "target object cannot be empty"),
    THE_OBJECT_COLLECTION_CANNOT_BE_EMPTY(10000, "The object collection cannot be empty"),
    VERSION_STATE_CANNOT_BE_EMPTY(10000, "bucket version state cannot be empty"),
    THE_SLIDER_IMAGE_FILE_DOES_NOT_EXIST(10000, "滑块验证码图为空,请检查"),
    THE_SLIDER_VERIFICATION_CODE_GENERATION_IS_ABNORMAL(10000, "滑块验证码生成异常,请重试"),
    THE_VERIFICATION_CODE_IS_OUT_OF_DATE_PLEASE_GET_A_NEW_ONE(10000, "验证码过时,请点击'换一张'"),
    THE_VERIFICATION_CODE_IS_INCORRECT(10000, "验证码不正确"),







    ;

    private final Integer code;
    private final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
