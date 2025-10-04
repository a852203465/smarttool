package cn.darkjrong.mail;

import cn.darkjrong.mail.domain.EmailTo;
import cn.darkjrong.spring.boot.autoconfigure.EmailProperties;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 电子邮件工具类测试类
 *
 * @author Rong.Jia
 * @date 2021/07/26 13:36:38
 */
public class EmailUtilsTest {

    private static EmailTemplate emailTemplate;

    private static List<EmailTo> toList = new ArrayList<>();
    private static List<EmailTo> bccList = new ArrayList<>();
    private static List<EmailTo> ccList = new ArrayList<>();
    private static List<EmailTo> replyList = new ArrayList<>();

    static {

        EmailTo to = new EmailTo();
        to.setMail("2111@qq.com");
        to.setName("aa");
        toList.add(to);

        EmailTo cc = new EmailTo();
        cc.setMail("2111@qq.com");
        cc.setName("aa");
        ccList.add(cc);

        EmailTo bcc = new EmailTo();
        bcc.setMail("2111@qq.com");
        bcc.setName("aa");
        bccList.add(bcc);

        EmailTo reply = new EmailTo();
        reply.setMail("2111@qq.com");
        reply.setName("aa");
        replyList.add(reply);

        EmailProperties emailProperties = new EmailProperties();
        emailProperties.setEnabled(Boolean.TRUE);
        emailProperties.setHost("smtp.qq.com");
        emailProperties.setPort(465);

        emailProperties.setSslEnable(Boolean.TRUE);
        emailProperties.setDebug(Boolean.FALSE);
        emailProperties.setName("贾荣");
        emailProperties.setUsername("2111@qq.com");
        emailProperties.setPassword("lenuiyivrybsbbbi");

        emailTemplate = new EmailTemplate(emailProperties);

    }

    @Test
    public void senText() {

        String sendText = emailTemplate.sendText( "对接测试", "This is a test mail ... :-)", toList);

        System.out.println(sendText);

    }

    @Test
    public void sendFile() {

        File file = new File("F:\\我的图片\\1.jpeg");

        String sendFile = emailTemplate.sendFile("对接测试发送文件", "This is a test mail ... :-)", file, toList);

        System.out.println(sendFile);

    }

    @Test
    public void sendHtml() {

        String html = ".... <img src=\"http://www.apache.org/images/feather.gif\"> ....";
        String sendHtml = emailTemplate.sendHtml("对接测试发送HTML", html, toList);
        System.out.println(sendHtml);

    }















}
