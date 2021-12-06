package org.humor.zxc.library.commons.util.utils;

import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


/**
 * 用java发送邮件的例子,支持：纯文本片段、html片段、附件、图片;
 */
public class EmailUtil {

    private static final int SMTP_SERVER_PORT = 25;
    private static final String SMTP_PROTOCOL = "smtp";
    private static final String SMTP_HOST_NAME = "smtp.***.com";
    private static final String SMTP_AUTH_USER = "humor@***.com";
    private static final String SMTP_AUTH_PWD = "******";
    private static final boolean SSL = false;
    private static final String CHARSET_UTF8 = "utf-8";

    /**
     * 多个接收者时,中间用空格' '分开
     *
     * @param receiverAddressSet 多个接收者的地址集合
     */
    public static Address[] builderMultiReceiver(Set<String> receiverAddressSet) {
        if (null == receiverAddressSet || receiverAddressSet.isEmpty()) {
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
    public static String makeMailSignatureDefault() {

        return "<br><br><br>" +
                "<br> <strong>ZXC</strong>" +
                "<br>www.zxc.com | ZXC" +
                "<br>ZXC" +
                "<br>地址. 中华人民共和国" +
                "<br>www.zxc.com";
    }


    /**
     * 通过默认账号，发送html格式的邮件
     *
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param receiverAddressSet 接收者地址集合
     * @return true:成功   false:失败
     */
    public static boolean sendHtmlMailDefault(String emailSubject, String htmlMailContent, Set<String> receiverAddressSet)
            throws Exception {
        return EmailUtil.sendHtmlMailHandler(null, null, emailSubject, htmlMailContent, receiverAddressSet, null, null);
    }

    /**
     * 发送html格式的邮件
     *
     * @param sender             发送者邮件地址
     * @param senderPwd          发送者密码
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param receiverAddressSet 接收者地址集合
     * @return true:成功   false:失败
     */
    public static boolean sendHtmlMail(String sender, String senderPwd, String emailSubject, String htmlMailContent, Set<String> receiverAddressSet)
            throws Exception {
        return EmailUtil.sendHtmlMailHandler(sender, senderPwd, emailSubject, htmlMailContent, receiverAddressSet, null, null);
    }

    /**
     * 发送html格式的邮件
     *
     * @param sender             发送者邮件地址
     * @param senderPwd          发送者密码
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param receiverAddressSet 接收者地址集合
     * @param ccAddressSet       抄送者地址集合（可空）
     * @param bccAddressSet      暗送者地址集合（可空）
     * @return true:成功   false:失败
     */
    public static boolean sendHtmlMailHandler(String sender, String senderPwd, String emailSubject, String htmlMailContent, Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
            throws Exception {
        try {
            if (StringUtils.isBlank(sender)) {
                sender = SMTP_AUTH_USER;
            }
            if (StringUtils.isBlank(senderPwd)) {
                senderPwd = SMTP_AUTH_PWD;
            }

            Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
            Properties props = new Properties();
            props.put("mail.transport.protocol", SMTP_PROTOCOL);
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", SMTP_SERVER_PORT);
            if (SSL) {
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            // 获得邮件会话对象
            Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
            // 创建MIME邮件对象
            MimeMessage mimeMessage = new MimeMessage(session);
            // 发件人
            mimeMessage.setFrom(new InternetAddress(sender));
            // 收件人
            mimeMessage.setRecipients(Message.RecipientType.TO, receivers);
            if (null != ccAddressSet && !ccAddressSet.isEmpty()) {
                Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
                //抄送
                mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);
            }
            if (null != bccAddressSet && !bccAddressSet.isEmpty()) {
                Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
                //暗送
                mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);
            }
            //防止中文乱码
            mimeMessage.setSubject(emailSubject, CHARSET_UTF8);
            // 发送日期
            mimeMessage.setSentDate(new Date());

            //邮件正文处理:普通文本片段、附件片段、图片片段
            // related意味着可以发送html格式的邮件
            //Multipart mp = new MimeMultipart("related");
            //混合片段,都可用
            Multipart mp = new MimeMultipart("alternative");

            // 正文
            BodyPart bodyPart = new MimeBodyPart();
            //html片段
            bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");
            mp.addBodyPart(bodyPart);
            // 设置邮件内容对象
            mimeMessage.setContent(mp);
            // 发送邮件
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }


    /**
     * 发送带有图片的邮件(注:如果邮件正文中含有图片,html邮件正文必须包括图片的链接部分)
     *
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param pictureFilePathSet 图片文件目录全路径集合,比如 /tmp/vclound/email_file/test.jpg
     * @param receiverAddressSet 接收者地址集合
     * @return true:成功   false:失败
     */
    public static boolean sendMultiPicturesMailHandler(String emailSubject, String htmlMailContent, Set<String> pictureFilePathSet, Set<String> receiverAddressSet)
            throws Exception {
        return EmailUtil.sendMultiPicturesMailHandler(null, null, emailSubject, htmlMailContent, pictureFilePathSet, receiverAddressSet, null, null);
    }

    /**
     * 发送带有图片的邮件
     *
     * @param sender             发送者邮件地址
     * @param senderPwd          发送者密码
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param pictureFilePathSet 图片文件目录全路径集合,比如 /tmp/vclound/email_file/test.jpg
     * @param receiverAddressSet 接收者地址集合
     * @param ccAddressSet       抄送者地址集合（可空）
     * @param bccAddressSet      暗送者地址集合（可空）
     * @return true:成功   false:失败
     */
    public static boolean sendMultiPicturesMailHandler(String sender, String senderPwd, String emailSubject, String htmlMailContent, Set<String> pictureFilePathSet, Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
            throws Exception {
        try {
            if (StringUtils.isBlank(sender)) {
                sender = SMTP_AUTH_USER;
            }
            if (StringUtils.isBlank(senderPwd)) {
                senderPwd = SMTP_AUTH_PWD;
            }

            Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
            Properties props = new Properties();
            props.put("mail.transport.protocol", SMTP_PROTOCOL);
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", SMTP_SERVER_PORT);
            if (SSL) {
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            // 获得邮件会话对象
            Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
            // 创建MIME邮件对象
            MimeMessage mimeMessage = new MimeMessage(session);
            // 发件人
            mimeMessage.setFrom(new InternetAddress(sender));
            // 收件人
            mimeMessage.setRecipients(Message.RecipientType.TO, receivers);
            if (null != ccAddressSet && !ccAddressSet.isEmpty()) {
                Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
                //抄送
                mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);
            }
            if (null != bccAddressSet && !bccAddressSet.isEmpty()) {
                Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
                //暗送
                mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);
            }
            //防止中文乱码
            mimeMessage.setSubject(emailSubject, CHARSET_UTF8);
            // 发送日期
            mimeMessage.setSentDate(new Date());

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

            // 设置邮件内容对象
            mimeMessage.setContent(mp);
            mimeMessage.saveChanges();
            // 发送邮件
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }


    /**
     * 发送带有图片的邮件
     *
     * @param sender             发送者邮件地址
     * @param senderPwd          发送者密码
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param attachFilePathSet  多个附加全路径的集合
     * @param pictureFilePathSet 图片文件目录全路径集合,比如 /tmp/vclound/email_file/test.jpg
     * @param receiverAddressSet 接收者地址集合
     * @param ccAddressSet       抄送者地址集合（可空）
     * @param bccAddressSet      暗送者地址集合（可空）
     * @return true:成功   false:失败
     */
    public static boolean sendMultiAttachMailHandler(String sender, String senderPwd, String emailSubject,
                                                     String htmlMailContent, Set<String> attachFilePathSet, Set<String> pictureFilePathSet,
                                                     Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
            throws Exception {
        try {
            if (StringUtils.isBlank(sender)) {
                sender = SMTP_AUTH_USER;
            }
            if (StringUtils.isBlank(senderPwd)) {
                senderPwd = SMTP_AUTH_PWD;
            }

            Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
            Properties props = new Properties();
            props.put("mail.transport.protocol", SMTP_PROTOCOL);
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", SMTP_SERVER_PORT);
            if (SSL) {
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            // 获得邮件会话对象
            Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
            // 创建MIME邮件对象
            MimeMessage mimeMessage = new MimeMessage(session);
            // 发件人
            mimeMessage.setFrom(new InternetAddress(sender));
            // 收件人
            mimeMessage.setRecipients(Message.RecipientType.TO, receivers);
            if (null != ccAddressSet && !ccAddressSet.isEmpty()) {
                Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
                //抄送
                mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);
            }
            if (null != bccAddressSet && !bccAddressSet.isEmpty()) {
                Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
                //暗送
                mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);
            }
            //防止中文乱码
            mimeMessage.setSubject(emailSubject, CHARSET_UTF8);
            // 发送日期
            mimeMessage.setSentDate(new Date());

            //邮件正文处理:普通文本片段、附件片段、图片片段，related意味着可以发送html格式的邮件
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

            // 设置邮件内容对象
            mimeMessage.setContent(mp);
            mimeMessage.saveChanges();
            // 发送邮件
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    /**
     * 发送带有图片的邮件
     *
     * @param sender             发送者邮件地址
     * @param senderPwd          发送者密码
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param pictureFilePath    图片文件目录全路,比如 /tmp/vclound/email_file/test.jpg
     * @param receiverAddressSet 接收者地址集合
     * @param ccAddressSet       抄送者地址集合（可空）
     * @param bccAddressSet      暗送者地址集合（可空）
     * @return true:成功   false:失败
     */
    public static boolean sendPictureMailHandler(String sender, String senderPwd, String emailSubject, String htmlMailContent, String pictureFilePath, Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
            throws Exception {
        try {
            if (StringUtils.isBlank(sender)) {
                sender = SMTP_AUTH_USER;
            }
            if (StringUtils.isBlank(senderPwd)) {
                senderPwd = SMTP_AUTH_PWD;
            }

            Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
            Properties props = new Properties();
            props.put("mail.transport.protocol", SMTP_PROTOCOL);
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", SMTP_SERVER_PORT);
            if (SSL) {
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            // 获得邮件会话对象
            Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
            // 创建MIME邮件对象
            MimeMessage mimeMessage = new MimeMessage(session);
            // 发件人
            mimeMessage.setFrom(new InternetAddress(sender));
            // 收件人
            mimeMessage.setRecipients(Message.RecipientType.TO, receivers);
            if (null != ccAddressSet && !ccAddressSet.isEmpty()) {
                Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
                //抄送
                mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);
            }
            if (null != bccAddressSet && !bccAddressSet.isEmpty()) {
                Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
                //暗送
                mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);
            }
            //防止中文乱码
            mimeMessage.setSubject(emailSubject, CHARSET_UTF8);
            // 发送日期
            mimeMessage.setSentDate(new Date());

            //邮件正文处理:普通文本片段、附件片段、图片片段,related意味着可以发送html格式的邮件
            Multipart mp = new MimeMultipart("related");
            //Multipart mp = new MimeMultipart("alternative"); //混合片段,都可用

            //html正文：
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");
            mp.addBodyPart(bodyPart);

            // 图片
            String fileName = FileUtils.getFileName(pictureFilePath);
            MimeBodyPart imgBodyPart = new MimeBodyPart();
            byte[] bytes = FileUtils.readFileBytes(pictureFilePath);
            ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
            imgBodyPart.setDataHandler(new DataHandler(fileds));
            imgBodyPart.setFileName(fileName);
            imgBodyPart.setHeader("Content-ID", "<IMG1>");
            mp.addBodyPart(imgBodyPart);

            mimeMessage.setContent(mp);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    public static boolean sendMixMail(String sender, String senderPwd, String emailSubject, String painContent,
                                      String htmlMailContent, Set<String> receiverAddressSet) {
        try {
            if (StringUtils.isBlank(sender)) {
                sender = SMTP_AUTH_USER;
            }
            if (StringUtils.isBlank(senderPwd)) {
                senderPwd = SMTP_AUTH_PWD;
            }

            Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
            Properties props = new Properties();
            props.put("mail.transport.protocol", SMTP_PROTOCOL);
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", SMTP_SERVER_PORT);

            if (SSL) {
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            // 获得邮件会话对象
            Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
            // 创建MIME邮件对象
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(sender));
            mimeMessage.setRecipients(Message.RecipientType.TO, receivers);
            //mimeMessage.setRecipients(Message.RecipientType.CC, receivers);  //抄送
            //mimeMessage.setRecipients(Message.RecipientType.BCC, receivers); //暗送
            mimeMessage.setSubject(emailSubject, CHARSET_UTF8);
            mimeMessage.setSentDate(new Date());

            //邮件正文处理:普通文本片段、附件片段、图片片段
            //Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
            Multipart mp = new MimeMultipart("alternative");

            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(painContent);
            // 网页格式
            bodyPart.setDataHandler(new DataHandler("测<img src='http://a.hiphotos.baidu.com/image/pic/item/e7cd7b899e510fb3a78c787fdd33c895d0430c44.jpg' />试",
                    "text/html;charset=UTF-8"));
            //html片段
            bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");
            BodyPart attachBodyPart = new MimeBodyPart();// 普通附件
            BodyPart attachBodyPart2 = new MimeBodyPart();// 普通附件


            //FileDataSource fds = new FileDataSource("D:/不得不承认Zeroc-Ice是RPC王者完爆Dubbo-Thrift.docx");
            //附件
            FileDataSource fds = new FileDataSource("/users/abc");
            String fileName = MimeUtility.encodeText(fds.getName());
            attachBodyPart.setDataHandler(new DataHandler(fds));
            //attachBodyPart.setFileName(new sun.misc.BASE64Encoder().encode(fds.getName().getBytes(CHARSET)));// 解决附件名中文乱
            attachBodyPart.setFileName(fileName);
            mp.addBodyPart(attachBodyPart);

            FileDataSource fds2 = new FileDataSource("/users/abc");
            attachBodyPart2.setDataHandler(new DataHandler(fds2));
            attachBodyPart2.setFileName(MimeUtility.encodeText(fds2.getName()));
            mp.addBodyPart(attachBodyPart2);

            // 附件图标
            MimeBodyPart imgBodyPart = new MimeBodyPart();
            byte[] bytes = FileUtils.readFileBytes("/users/1.jpg");
            ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
            imgBodyPart.setDataHandler(new DataHandler(fileds));
            imgBodyPart.setFileName("1.jpg");
            //imgBodyPart.setHeader("Content-ID", "<img1></img1>");// 在html中使用该图片方法src="cid:IMG1"
            imgBodyPart.setHeader("Content-ID", "<image1>");
            mp.addBodyPart(imgBodyPart);
            mp.addBodyPart(bodyPart);
            mimeMessage.setContent(mp);
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public static void main(String[] args) throws Exception {
        Set<String> receiverAddressSet = new HashSet<>();
        receiverAddressSet.add("778521003@qq.com");
        receiverAddressSet.add("1640317717@qq.com");

        Set<String> ccAddressSet = new HashSet<>();

        Set<String> bccAddressSet = new HashSet<>();


        String emailHtmlContent = "";
        emailHtmlContent += "<table width=\"700\" border=\"1\" style=\"font-size:12px\">";
        emailHtmlContent += "<tr>";
        emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>序号</td>";
        emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>项目</td>";
        emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>主机名称</td>";
        emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>监控类型</td>";
        emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>监控</td>";
        emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>监控</td>";
        emailHtmlContent += "</tr>";
        emailHtmlContent += "</table>";
        emailHtmlContent += EmailUtil.makeMailSignatureDefault();

        EmailUtil.sendHtmlMailDefault("测试邮件发送888", emailHtmlContent, receiverAddressSet);

        String htmlMailContent = "<b>Some <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG1\"><br>good!";
        Set<String> pictureFilePathSet = new HashSet<>();
        pictureFilePathSet.add("/Users/Downloads/banner.png");
        pictureFilePathSet.add("/Users/Downloads/detail_banner.png");
//        EmailUtil.sendPictureMailHandler(null, null, "测试图片邮件发送888", htmlMailContent, pictureFilePath, receiverAddressSet, null, null);

        htmlMailContent += "<b>第一张 Some <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG1\"><br>good!";
        htmlMailContent += "<br><b>第二张 <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG2\"><br>good!";
        EmailUtil.sendMultiPicturesMailHandler("", null, "男人看了会沉默，女人看了会流泪。", htmlMailContent, pictureFilePathSet, receiverAddressSet, ccAddressSet, bccAddressSet);
    }

    /**
     * SMTP身份验证
     */
    public static class SmtpAuthenticator extends Authenticator {
        String username = null;
        String password = null;

        public SmtpAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.username, this.password);
        }
    }

    public static class ByteArrayDataSource implements DataSource {
        private final String contentType;
        private final byte[] buf;
        private final int len;

        public ByteArrayDataSource(byte[] buf, String contentType) {
            this(buf, buf.length, contentType);
        }

        public ByteArrayDataSource(byte[] buf, int length, String contentType) {
            this.buf = buf;
            this.len = length;
            this.contentType = contentType;
        }

        @Override
        public String getContentType() {
            if (contentType == null) {
                return "application/octet-stream";
            }
            return contentType;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(buf, 0, len);
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public OutputStream getOutputStream() {
            throw new UnsupportedOperationException();
        }

    }
}


