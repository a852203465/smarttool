package cn.darkjrong.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * i18n工具类
 *
 * @author Rong.Jia
 * @date 2025/03/27
 */
@Slf4j
public class I18nTemplate {

    private final MessageSourceAccessor messageSourceAccessor;

    public I18nTemplate(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * 获取消息
     * @param code 编码
     * @return {@link String }
     */
    public String getMessage(String code) {
        return messageSourceAccessor.getMessage(code);
    }











}
