package xiaozhuo.info.service.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil
{

    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public static void sendMail(String from, String paw, String to, String emailTemp, String title, String... strings) {
        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com"; // QQ 邮件服务器
        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");

        properties.put("mail.transport.protocol", "smtp");// 协议名称
        properties.put("mail.smtp.socketFactory.port", 465); // 服务器端口
        properties.put("mail.smtp.starttls.enable", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, paw); // 发件人邮件用户名、密码
            }
        });
        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: 头部头字段
            message.setSubject(title);
            // 设置消息体
            String content = String.format(emailTemp, strings);
            // 设置消息体
            MimeBodyPart text = new MimeBodyPart();
            text.setContent("<img src='cid:a'>" + content, "text/html;charset=utf-8");
            //MimeBodyPart img = new MimeBodyPart();
            //DataHandler dh = new DataHandler(new FileDataSource("/data/dleye/img/rz.png"));
            //img.setDataHandler(dh);
            //img.setContentID("a");
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            //mm.addBodyPart(img);
            mm.setSubType("related");
            MimeBodyPart all = new MimeBodyPart();
            all.setContent(mm);
            MimeMultipart mm2 = new MimeMultipart();
            mm2.addBodyPart(all);
            mm2.setSubType("mixed");
            message.setContent(mm2);
            message.saveChanges();
            Transport.send(message);
            logger.info("Sent message:(" + content + ")successfully");
        }
        catch (MessagingException mex) {
            logger.error("sendMail is exception:" + mex.toString());
        }
    }

}
