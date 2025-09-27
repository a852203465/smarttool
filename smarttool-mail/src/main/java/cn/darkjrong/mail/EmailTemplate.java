package cn.darkjrong.mail;

import cn.darkjrong.core.enums.ErrorEnum;
import cn.darkjrong.mail.domain.EmailTo;
import cn.darkjrong.mail.exceptions.MailException;
import cn.darkjrong.spring.boot.autoconfigure.EmailProperties;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;
import org.apache.commons.mail.resolver.DataSourceCompositeResolver;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 电子邮件操作类
 *
 * @author Rong.Jia
 * @date 2021/07/27 08:36:31
 */
@Slf4j
public class EmailTemplate {

    /**
     * 电子邮件属性
     */
    private final EmailProperties emailProperties;

    public EmailTemplate(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    /**
     * 发送文本邮件
     *
     * @param subject  主题
     * @param message  消息
     * @param toEmails 收件人
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails) throws MailException {
        return this.sendText(subject, message, toEmails, Collections.emptyList());
    }

    /**
     * 发送文本邮件
     *
     * @param subject  主题
     * @param message  消息
     * @param toEmail 收件人
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           EmailTo toEmail) throws MailException {

        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());

        return this.sendText(subject, message, Collections.singletonList(toEmail));
    }

    /**
     * 发送文本邮件
     *
     * @param subject   主题
     * @param message   消息
     * @param toEmails  收件邮箱
     * @param ccEmails 抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails) throws MailException {
        return this.sendText(subject, message, toEmails, ccEmails, Collections.emptyList());
    }

    /**
     * 发送文本邮件
     *
     * @param subject   主题
     * @param message   消息
     * @param toEmails  收件人
     * @param bccEmails 密送邮箱
     * @param ccEmails  抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails) throws MailException {
        return this.sendText(subject, message, toEmails, ccEmails, bccEmails, Collections.emptyList());
    }

    /**
     * 发送文本邮件
     *
     * @param subject     主题
     * @param message     消息
     * @param toEmails    收件人
     * @param bccEmails   bcc邮箱
     * @param ccEmails    抄送邮箱
     * @param replyEmails 回复邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails) throws MailException {

        return this.sendText(subject, message, toEmails, ccEmails, bccEmails, replyEmails, null);
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param toEmails   收件邮件
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment, toEmails, Collections.emptyList());
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param toEmail   收件邮件
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           EmailTo toEmail) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());

        return this.sendFile(subject, message, attachment, Collections.singletonList(toEmail));
    }

    /**
     * 发送文件邮件
     *
     * @param subject     主题
     * @param message     消息
     * @param attachment  文件
     * @param toEmails    收件邮件
     * @param bccEmails   抄送邮箱
     * @param ccEmails    抄送邮箱
     * @param replyEmails 回复邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        return this.sendFile(subject, message, attachment.getPath(), toEmails, ccEmails, bccEmails, replyEmails);
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param toEmails   邮件
     * @param bccEmails  密送邮箱
     * @param ccEmails   抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, bccEmails, Collections.emptyList());
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param toEmails   邮件
     * @param ccEmails  抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, Collections.emptyList());
    }

    /**
     * 发送文件邮件
     *
     * @param subject     主题
     * @param message     消息
     * @param attachment  附件
     * @param toEmails    邮件
     * @param bccEmails   密送邮箱
     * @param ccEmails    抄送邮箱
     * @param replyEmails 回复邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment, List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

       return this.sendFile(subject, message, attachment, toEmails, ccEmails, bccEmails, replyEmails, null);
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmails   邮件
     * @param bccEmails  密送邮箱
     * @param ccEmails   抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment, List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, bccEmails, Collections.emptyList());
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmails   邮件
     * @param ccEmails  抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, Collections.emptyList());
    }

    /**
     * 发送文件
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmails   邮件
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment,
                           List<EmailTo> toEmails) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        return this.sendFile(subject, message, attachment, toEmails, Collections.emptyList());
    }

    /**
     * 发送文件
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmail   邮件
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment, EmailTo toEmail) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        return this.sendFile(subject, message, attachment, Collections.singletonList(toEmail));
    }

    /**
     * 发送html
     *
     * @param subject  主题
     * @param html     超文本标记语言
     * @param toEmails 邮件
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails) throws MailException {

        return this.sendHtml(subject, html, toEmails, Collections.emptyList());
    }

    /**
     * 发送html
     *
     * @param subject  主题
     * @param html     超文本标记语言
     * @param toEmail 邮件
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html, EmailTo toEmail) throws MailException {

        Assert.notBlank(html, ErrorEnum.THE_MESSAGE_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());

        return this.sendHtml(subject, html, Collections.singletonList(toEmail), Collections.emptyList());
    }

    /**
     * 发送html
     *
     * @param subject     主题
     * @param html        超文本标记语言
     * @param toEmails    邮件
     * @param bccEmails   密送邮箱
     * @param ccEmails    抄送邮箱
     * @param replyEmails 回复邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails) throws MailException {

        Assert.notBlank(html, ErrorEnum.THE_MESSAGE_CANNOT_BE_EMPTY.getMessage());

       return this.sendHtml(subject, html, toEmails, ccEmails, bccEmails, replyEmails, null);
    }

    /**
     * 发送html
     *
     * @param subject   主题
     * @param html      超文本标记语言
     * @param toEmails  邮件
     * @param bccEmails 密送邮箱
     * @param ccEmails  抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails) throws MailException {

        return this.sendHtml(subject, html, toEmails, ccEmails, bccEmails, Collections.emptyList());
    }

    /**
     * 发送html
     *
     * @param subject   主题
     * @param html      超文本标记语言
     * @param toEmails  邮件
     * @param ccEmails 抄送送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails) throws MailException {

        return this.sendHtml(subject, html, toEmails, ccEmails, Collections.emptyList());
    }

    /**
     * 发送文本邮件
     *
     * @param subject  主题
     * @param message  消息
     * @param toEmails 收件人
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails, Date date) throws MailException {
        return this.sendText(subject, message, toEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文本邮件
     *
     * @param subject  主题
     * @param message  消息
     * @param toEmail 收件人
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           EmailTo toEmail, Date date) throws MailException {

        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());

        return this.sendText(subject, message, Collections.singletonList(toEmail), date);
    }

    /**
     * 发送文本邮件
     *
     * @param subject   主题
     * @param message   消息
     * @param toEmails  收件邮箱
     * @param ccEmails 抄送邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails, Date date) throws MailException {
        return this.sendText(subject, message, toEmails, ccEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文本邮件
     *
     * @param subject   主题
     * @param message   消息
     * @param toEmails  收件人
     * @param bccEmails 密送邮箱
     * @param ccEmails  抄送邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails, Date date) throws MailException {
        return this.sendText(subject, message, toEmails, ccEmails, bccEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文本邮件
     *
     * @param subject     主题
     * @param message     消息
     * @param toEmails    收件人
     * @param bccEmails   bcc邮箱
     * @param ccEmails    抄送邮箱
     * @param replyEmails 回复邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendText(String subject, String message,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails, Date date) throws MailException {

        try {

            Email email = new SimpleEmail();
            register(email, subject, message, toEmails, ccEmails, bccEmails, replyEmails, date);

            return email.send();
        }catch (Exception e) {
            log.error(String.format("********,sendText,subject【%s】,message【%s】发送异常,【%s】", subject, message, e.getMessage()), e);
            throw new MailException(e.getMessage());
        }
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param toEmails   收件邮件
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails, Date date) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment, toEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param date 发送时间
     * @param toEmail   收件邮件
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           EmailTo toEmail, Date date) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());

        return this.sendFile(subject, message, attachment, Collections.singletonList(toEmail), date);
    }

    /**
     * 发送文件邮件
     *
     * @param subject     主题
     * @param message     消息
     * @param attachment  文件
     * @param toEmails    收件邮件
     * @param bccEmails   抄送邮箱
     * @param ccEmails    抄送邮箱
     * @param date 发送时间
     * @param replyEmails 回复邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails,
                           Date date) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        return this.sendFile(subject, message, attachment.getPath(),
                toEmails, ccEmails, bccEmails, replyEmails, date);
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param toEmails   邮件
     * @param bccEmails  密送邮箱
     * @param ccEmails   抄送邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails, Date date) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, bccEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 文件
     * @param toEmails   邮件
     * @param date 发送时间
     * @param ccEmails  抄送邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           File attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails, Date date) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment.getPath()), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文件邮件
     *
     * @param subject     主题
     * @param message     消息
     * @param attachment  附件
     * @param toEmails    邮件
     * @param bccEmails   密送邮箱
     * @param ccEmails    抄送邮箱
     * @param date 发送时间
     * @param replyEmails 回复邮箱
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment, List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails, Date date) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        EmailAttachment emailAttachment = new EmailAttachment();

        try {

            if (EmailUtils.checkOnlineFile(attachment)) {
                emailAttachment.setURL(new URL(attachment));
            }else {
                emailAttachment.setPath(attachment);
            }

            emailAttachment.setName(FileUtil.getName(attachment));
            emailAttachment.setDescription(FileUtil.getName(attachment));
            emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);

            MultiPartEmail email = new MultiPartEmail();
            email.setBoolHasAttachments(Boolean.TRUE);

            register(email, subject, message, toEmails, ccEmails, bccEmails, replyEmails, date);
            email.attach(emailAttachment);

            return email.send();
        } catch (Exception e) {
            log.error(String.format("********,sendFile,subject【%s】,message【%s】发送异常,【%s】", subject, message, e.getMessage()), e);
            throw new MailException(e.getMessage());
        }
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmails   邮件
     * @param bccEmails  密送邮箱
     * @param ccEmails   抄送邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment, List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails, Date date) throws MailException {

        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());

        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, bccEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmails   邮件
     * @param ccEmails  抄送邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails, Date date) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        return this.sendFile(subject, message, attachment,
                toEmails, ccEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文件
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmails   邮件
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment,
                           List<EmailTo> toEmails, Date date) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        return this.sendFile(subject, message, attachment, toEmails, Collections.emptyList(), date);
    }

    /**
     * 发送文件
     * 发送文件邮件
     *
     * @param subject    主题
     * @param message    消息
     * @param attachment 附件
     * @param toEmail   邮件
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendFile(String subject, String message,
                           String attachment, EmailTo toEmail, Date date) throws MailException {
        Assert.isTrue(EmailUtils.checkFileExists(attachment), ErrorEnum.ATTACHMENT_DOES_NOT_EXIST.getMessage());
        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        return this.sendFile(subject, message, attachment, Collections.singletonList(toEmail), date);
    }

    /**
     * 发送html
     *
     * @param subject  主题
     * @param html     超文本标记语言
     * @param toEmails 邮件
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails, Date date) throws MailException {

        return this.sendHtml(subject, html, toEmails, Collections.emptyList(), date);
    }

    /**
     * 发送html
     *
     * @param subject  主题
     * @param html     超文本标记语言
     * @param toEmail 邮件
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html, EmailTo toEmail, Date date) throws MailException {

        Assert.notBlank(html, ErrorEnum.THE_MESSAGE_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail, ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(toEmail.getMail(), ErrorEnum.THE_RECEIVER_CANNOT_BE_EMPTY.getMessage());

        return this.sendHtml(subject, html, Collections.singletonList(toEmail), Collections.emptyList(), date);
    }

    /**
     * 发送html
     *
     * @param subject     主题
     * @param html        超文本标记语言
     * @param toEmails    邮件
     * @param bccEmails   密送邮箱
     * @param ccEmails    抄送邮箱
     * @param replyEmails 回复邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails,
                           List<EmailTo> replyEmails, Date date) throws MailException {

        Assert.notBlank(html, ErrorEnum.THE_MESSAGE_CANNOT_BE_EMPTY.getMessage());

        ImageHtmlEmail email = new ImageHtmlEmail();
        try {
            email.setDataSourceResolver(new DataSourceCompositeResolver(EmailUtils.getDataSourceResolvers()));
            register(email, subject, html, toEmails, ccEmails, bccEmails, replyEmails, date);
            email.setHtmlMsg(html);
            email.setTextMsg(ErrorEnum.YOUR_EMAIL_CLIENT_DOES_NOT_SUPPORT_HTML_MESSAGES.getMessage());

            return email.send();
        }catch (Exception e) {
            log.error(String.format("********,sendHtml,subject【%s】,message【%s】发送异常,【%s】", subject, html, e.getMessage()), e);
            throw new MailException(e.getMessage());
        }
    }

    /**
     * 发送html
     *
     * @param subject   主题
     * @param html      超文本标记语言
     * @param toEmails  邮件
     * @param bccEmails 密送邮箱
     * @param ccEmails  抄送邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails,
                           List<EmailTo> bccEmails, Date date) throws MailException {

        return this.sendHtml(subject, html, toEmails, ccEmails, bccEmails, Collections.emptyList(), date);
    }

    /**
     * 发送html
     *
     * @param subject   主题
     * @param html      超文本标记语言
     * @param toEmails  邮件
     * @param ccEmails 抄送送邮箱
     * @param date 发送时间
     * @return {@link String} 邮件消息ID
     * @throws MailException 电子邮件异常
     */
    public String sendHtml(String subject, String html,
                           List<EmailTo> toEmails,
                           List<EmailTo> ccEmails, Date date) throws MailException {

        return this.sendHtml(subject, html, toEmails, ccEmails, Collections.emptyList(), date);
    }


































    /**
     * 注册信息
     *
     * @param email       电子邮件
     * @param subject     主题
     * @param toEmails    收件人
     * @param bccEmails   密送收件人
     * @param ccEmails    抄送人
     * @param replyEmails 回复人
     * @param message     消息
     * @throws MailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                              List<EmailTo> toEmails,
                              List<EmailTo> ccEmails,
                              List<EmailTo> bccEmails,
                              List<EmailTo> replyEmails) throws EmailException {

        register(email, subject, message, toEmails, ccEmails, bccEmails, replyEmails, null);
    }

    /**
     * 注册信息
     *
     * @param email     电子邮件
     * @param subject   主题
     * @param toEmails  收件人
     * @param bccEmails 密送收件人
     * @param message   消息
     * @throws EmailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                              List<EmailTo> toEmails,
                          List<EmailTo> bccEmails)  throws EmailException {
        this.register(email, subject, message, toEmails, bccEmails, Collections.emptyList());
    }

    /**
     * 注册信息
     *
     * @param email     电子邮件
     * @param subject   主题
     * @param toEmails  收件人
     * @param message   消息
     * @param bccEmails 密送邮箱
     * @param ccEmails  抄送邮箱
     * @throws EmailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                              List<EmailTo> toEmails,
                              List<EmailTo> ccEmails,
                              List<EmailTo> bccEmails)  throws EmailException {
        this.register(email, subject, message, toEmails, ccEmails, bccEmails, Collections.emptyList());
    }

    /**
     * 注册信息
     *
     * @param email    电子邮件
     * @param subject  主题
     * @param toEmails 收件人
     * @param message  消息
     * @throws EmailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                              List<EmailTo> toEmails)  throws EmailException {
        this.register(email, subject, message, toEmails, Collections.emptyList());
    }

    /**
     * 注册信息
     *
     * @param email       电子邮件
     * @param subject     主题
     * @param toEmails    收件人
     * @param bccEmails   密送收件人
     * @param ccEmails    抄送人
     * @param date 发送时间
     * @param replyEmails 回复人
     * @param message     消息
     * @throws MailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                          List<EmailTo> toEmails,
                          List<EmailTo> ccEmails,
                          List<EmailTo> bccEmails,
                          List<EmailTo> replyEmails,
                          Date date) throws EmailException {

        Assert.notBlank(subject, ErrorEnum.THE_TOPIC_CANNOT_BE_EMPTY.getMessage());
        Assert.notBlank(message, ErrorEnum.THE_MESSAGE_CANNOT_BE_EMPTY.getMessage());

        email.setStartTLSEnabled(emailProperties.getStartTlsEnabled());
        email.setStartTLSRequired(emailProperties.getStartTlsRequired());
        email.setSendPartial(emailProperties.getSendPartial());
        email.setSSLCheckServerIdentity(emailProperties.getSslCheckServerIdentity());
        email.setHostName(emailProperties.getHost());
        EmailUtils.setPort(email, emailProperties.getSslEnable(), emailProperties.getPort());
        if (!emailProperties.getAvoidAuthEnable()) {
            email.setAuthentication(emailProperties.getUsername(), emailProperties.getPassword());
        }
        email.setSSLOnConnect(emailProperties.getSslEnable());
        email.setCharset(emailProperties.getCharset());
        email.setSubject(subject);
        email.setFrom(emailProperties.getUsername(),
                emailProperties.getName(), emailProperties.getCharset());
        EmailUtils.setDate(email, date);
        EmailUtils.setMsg(email, message);
        email.setDebug(emailProperties.getDebug());
        email.setSocketTimeout(emailProperties.getTimeout());
        email.setSocketConnectionTimeout(emailProperties.getConnectionTimeout());

        if (CollectionUtil.isEmpty(toEmails) && CollectionUtil.isEmpty(ccEmails)
                && CollectionUtil.isEmpty(bccEmails)) {
            log.error("Can not recipient, BCC person, CC person at the same time for empty");
            throw new EmailException(ErrorEnum.CAN_NOT_RECIPIENT_BCC_CC_AT_THE_SAME_TIME_FOR_EMPTY.getMessage());
        }
        EmailUtils.addTo(email, toEmails);
        EmailUtils.addCc(email, ccEmails);
        EmailUtils.addBcc(email, bccEmails);
        EmailUtils.addReply(email, replyEmails);
        EmailUtils.addPop3(email, emailProperties.getPop3());
        EmailUtils.setBounce(email, emailProperties.getBounceEnable(),
                emailProperties.getUsername());

    }

    /**
     * 注册信息
     *
     * @param email     电子邮件
     * @param subject   主题
     * @param toEmails  收件人
     * @param date 发送时间
     * @param bccEmails 密送收件人
     * @param message   消息
     * @throws EmailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                          List<EmailTo> toEmails,
                          List<EmailTo> bccEmails, Date date)  throws EmailException {
        this.register(email, subject, message, toEmails,
                bccEmails, Collections.emptyList(), date);
    }

    /**
     * 注册信息
     *
     * @param email     电子邮件
     * @param subject   主题
     * @param toEmails  收件人
     * @param message   消息
     * @param date 发送时间
     * @param bccEmails 密送邮箱
     * @param ccEmails  抄送邮箱
     * @throws EmailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                          List<EmailTo> toEmails,
                          List<EmailTo> ccEmails,
                          List<EmailTo> bccEmails, Date date)  throws EmailException {
        this.register(email, subject, message, toEmails,
                ccEmails, bccEmails, Collections.emptyList(), date);
    }

    /**
     * 注册信息
     *
     * @param email    电子邮件
     * @param subject  主题
     * @param toEmails 收件人
     * @param date 发送时间
     * @param message  消息
     * @throws EmailException 电子邮件异常
     */
    private void register(Email email, String subject, String message,
                          List<EmailTo> toEmails, Date date)  throws EmailException {
        this.register(email, subject, message, toEmails, Collections.emptyList(), date);
    }




































}
