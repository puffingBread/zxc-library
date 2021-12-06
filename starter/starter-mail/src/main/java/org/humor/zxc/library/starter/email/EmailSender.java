package org.humor.zxc.library.starter.email;

import org.apache.commons.lang3.StringUtils;
import org.humor.zxc.library.commons.util.utils.EmailUtil;
import org.humor.zxc.library.commons.util.utils.FileUtils;
import org.humor.zxc.library.starter.email.config.EmailProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

/**
 * Date: 2021/12/6
 * Time: 3:57 PM
 *
 * @author xuzz
 */
@Component
public class EmailSender {

    @Resource
    private EmailProperties emailProperties;

    public boolean sendMailHandler(String sender, String senderPwd, String emailSubject,
                                   String htmlMailContent, Set<String> pictureFilePathSet,
                                   Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet) {
        Address[] receivers = this.builderMultiReceiver(receiverAddressSet);
        Properties props = this.buildProperties(sender, senderPwd);
        // 获得邮件会话对象
        Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
        // 创建MIME邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            // 发件人
            mimeMessage.setFrom(new InternetAddress(sender));
            // 收件人
            mimeMessage.setRecipients(Message.RecipientType.TO, receivers);
            // 抄送
            if (!CollectionUtils.isEmpty(ccAddressSet)) {
                Address[] ccReceivers = this.builderMultiReceiver(ccAddressSet);
                mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);
            }
            // 暗送
            if (!CollectionUtils.isEmpty(bccAddressSet)) {
                Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
                mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);
            }
            //防止中文乱码
            mimeMessage.setSubject(emailSubject, "utf-8");
            // 发送日期
            mimeMessage.setSentDate(new Date());

            // multipart构建
            Multipart mp;
            if (CollectionUtils.isEmpty(pictureFilePathSet)) {
                mp = this.buildHtmlMultipart(htmlMailContent);
            } else {
                mp = this.buildMultiPicturesMultipart(htmlMailContent, pictureFilePathSet);
            }
            // 设置邮件内容对象
            mimeMessage.setContent(mp);
            mimeMessage.saveChanges();
            // 发送邮件
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }


    public Multipart buildHtmlMultipart(String htmlMailContent) throws MessagingException {
        // 邮件正文处理:普通文本片段、附件片段、图片片段
        // related意味着可以发送html格式的邮件
        //Multipart mp = new MimeMultipart("related");
        // 混合片段,都可用
        Multipart mp = new MimeMultipart("alternative");
        // 正文
        BodyPart bodyPart = new MimeBodyPart();
        //html片段
        bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");
        mp.addBodyPart(bodyPart);
        return mp;
    }

    public Multipart buildMultiPicturesMultipart(String htmlMailContent, Set<String> pictureFilePathSet) throws MessagingException {
        //邮件正文处理:普通文本片段、附件片段、图片片段
        // related意味着可以发送html格式的邮件,related
        Multipart mp = new MimeMultipart("related");
        //Multipart mp = new MimeMultipart("alternative"); //混合片段,都可用

        //html正文：
        BodyPart bodyPart = new MimeBodyPart();
        //html片段
        bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");
        mp.addBodyPart(bodyPart);

        int i = 1;
        for (String pictureFilePath : pictureFilePathSet) {
            // 图片
            String fileName = FileUtils.getFileName(pictureFilePath);
            // 附件图标
            MimeBodyPart imgBodyPart = new MimeBodyPart();
            byte[] bytes = FileUtils.readFileBytes(pictureFilePath);
            ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
            imgBodyPart.setDataHandler(new DataHandler(fileds));
            imgBodyPart.setFileName(fileName);
            // !!!!注意这里是"<IMG1>" 带有尖括号 而在正文的html里面则是src="cid:IMG1"
            imgBodyPart.setHeader("Content-ID", "<IMG" + i + ">");
            mp.addBodyPart(imgBodyPart);

            i++;
        }
        return mp;
    }

    private Properties buildProperties(String sender, String senderPwd) {
        if (StringUtils.isBlank(sender)) {
            sender = emailProperties.getAuthUser();
        }
        if (StringUtils.isBlank(senderPwd)) {
            senderPwd = emailProperties.getAuthPassword();
        }

        Properties props = new Properties();
        props.put("mail.transport.protocol", emailProperties.getProtocol());
        props.put("mail.smtp.host", emailProperties.getHost());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", emailProperties.getServerPort());
        if (emailProperties.getSsl()) {
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        return props;
    }

    /**
     * 多个接收者时邮箱地址构建
     *
     * @param receiverAddressSet 多个接收者的地址集合
     */
    private Address[] builderMultiReceiver(Set<String> receiverAddressSet) {
        if (CollectionUtils.isEmpty(receiverAddressSet)) {
            return null;
        }

        Address[] internetAddress = new InternetAddress[receiverAddressSet.size()];
        int i = 0;
        for (String receiver : receiverAddressSet) {
            if (StringUtils.isBlank(receiver)) {
                continue;
            }
            try {
                internetAddress[i] = new InternetAddress(receiver);
            } catch (AddressException e) {
                e.printStackTrace();
            }
            i++;
        }
        return internetAddress;
    }

    /**
     * 邮件的签名片段
     */
    public String makeMailSignatureDefault() {

        return "<br><br><br>" +
                "<br> <strong>ZXC</strong>" +
                "<br>www.zxc.com | ZXC" +
                "<br>ZXC" +
                "<br>地址. 中华人民共和国" +
                "<br>www.zxc.com";
    }
}
