//package com.bread.zxc.common.email;
//
//
//import com.bread.zxc.common.utils.BlankUtil;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Properties;
//import java.util.Set;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.Address;
//import javax.mail.Authenticator;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.mail.internet.MimeUtility;
//
//
///**
// * 用java发送邮件的例子,支持：纯文本片段、html片段、附件、图片;
// * @author aq.li@vipstack.net
// * @since v1.0
// * @date 2016年7月1日 上午11:42:24
// */
//public class EmailUtil {
//	private static final int SMTP_SERVER_PORT = 25;
//	private static final String SMTP_PROTOCOL = "smtp";
//	private static final String SMTP_HOST_NAME = "smtp.smartconns.com";
//    private static final String SMTP_AUTH_USER = "xuzeng@smartconns.com";
//    private static final String SMTP_AUTH_PWD  = "Xz15919672519.";
//    private static final boolean ssl = false;
//    private static final String CHARSET_UTF8  = "utf-8";
//
//    /**
//     * 多个接收者时,中间用空格' '分开
//     * @param receiverAddressSet 多个接收者的地址集合
//     * @return
//     */
//    public static Address[] builderMultiReceiver(Set<String> receiverAddressSet){
//    	if(null == receiverAddressSet || receiverAddressSet.isEmpty()){
//    		return null;
//    	}
//
//    	Address[] internetAddress = new InternetAddress[receiverAddressSet.size()];
//    	int i = 0;
//    	for(String receiver : receiverAddressSet){
//    		if(BlankUtil.isBlank(receiver)){
//    			continue;
//    		}
//    		try {
//				internetAddress[i] = new InternetAddress(receiver);
//			} catch (AddressException e) {
//				//e.printStackTrace();
//			}
//    		i++;
//    	}
//    	return internetAddress;
//    }
//
//    /**
//     * 邮件的签名片段
//     * @return
//     */
//    public static String makeMailSignatureDefault(){
//    	StringBuffer sb = new StringBuffer();
//    	sb.append("<br><br><br>");
//    	sb.append("<br> <strong>ZXC</strong>");
//    	sb.append("<br>www.zxc.com | ZXC");
//    	sb.append("<br>ZXC");
//    	sb.append("<br>地址. 中华人民共和国");
//    	sb.append("<br>www.zxc.com");
//
//    	return sb.toString();
//    }
//
//
//    /**
//     * 通过麒云官方的notice@vipstack.net帐号，发送html格式的邮件
//     * @param emailSubject 邮件主题
//     * @param htmlMailContent html格式的邮件内容
//     * @param receiverAddressSet 接收者地址集合
//     * @return true:成功   false:失败
//     * @throws Exception
//     */
//    public static boolean sendHtmlMailDefault(String emailSubject, String htmlMailContent, Set<String> receiverAddressSet)
//    		throws Exception {
//		return EmailUtil.sendHtmlMailHandler(null, null, emailSubject, htmlMailContent, receiverAddressSet, null, null);
//	}
//
//    /**
//     * 发送html格式的邮件
//     * @param sender 发送者邮件地址
//     * @param senderPwd 发送者密码
//     * @param emailSubject 邮件主题
//     * @param htmlMailContent html格式的邮件内容
//     * @param receiverAddressSet 接收者地址集合
//     * @return true:成功   false:失败
//     * @throws Exception
//     */
//    public static boolean sendHtmlMail(String sender, String senderPwd, String emailSubject, String htmlMailContent, Set<String> receiverAddressSet)
//    		throws Exception {
//		return EmailUtil.sendHtmlMailHandler(sender, senderPwd, emailSubject, htmlMailContent, receiverAddressSet, null, null);
//	}
//
//    /**
//     * 发送html格式的邮件
//     * @param sender 发送者邮件地址
//     * @param senderPwd 发送者密码
//     * @param emailSubject 邮件主题
//     * @param htmlMailContent html格式的邮件内容
//     * @param receiverAddressSet 接收者地址集合
//     * @param ccAddressSet 抄送者地址集合（可空）
//     * @param bccAddressSet 暗送者地址集合（可空）
//     * @return true:成功   false:失败
//     * @throws Exception
//     */
//    public static boolean sendHtmlMailHandler(String sender, String senderPwd, String emailSubject, String htmlMailContent, Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
//    		throws Exception {
//		try{
//			if(BlankUtil.isBlank(sender)){
//				sender = SMTP_AUTH_USER;
//			}
//			if(BlankUtil.isBlank(senderPwd)){
//				senderPwd = SMTP_AUTH_PWD;
//			}
//
//			Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
//			Properties props = new Properties();
//	        props.put("mail.transport.protocol", SMTP_PROTOCOL);
//	        props.put("mail.smtp.host", SMTP_HOST_NAME);
//	        props.put("mail.smtp.auth", "true");
//	        props.put("mail.smtp.port", SMTP_SERVER_PORT);
//	        if(ssl){
//	        	props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//	        }
//			// 获得邮件会话对象
//			Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
//			/** *************************************************** */
//			// 创建MIME邮件对象
//			MimeMessage mimeMessage = new MimeMessage(session);
//			mimeMessage.setFrom(new InternetAddress(sender));// 发件人
//			mimeMessage.setRecipients(Message.RecipientType.TO, receivers);// 收件人
//			if(null != ccAddressSet && !ccAddressSet.isEmpty()){
//				Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);  //抄送
//			}
//			if(null != bccAddressSet && !bccAddressSet.isEmpty()){
//				Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);  //暗送
//			}
//			mimeMessage.setSubject(emailSubject, CHARSET_UTF8); //防止中文乱码
//			mimeMessage.setSentDate(new Date());// 发送日期
//
//			//邮件正文处理:普通文本片段、附件片段、图片片段
//			//Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
//			Multipart mp = new MimeMultipart("alternative"); //混合片段,都可用
//
//			/** *************************************************** */
//			BodyPart bodyPart = new MimeBodyPart();// 正文
//			bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");  //html片段
//			/** *************************************************** */
//			mp.addBodyPart(bodyPart);
//			mimeMessage.setContent(mp);// 设置邮件内容对象
//			Transport.send(mimeMessage);// 发送邮件
//		}catch(Exception  e){
//			e.printStackTrace();
//			throw e;
//		}
//		return true;
//	}
//
//    /**
//     * 获取文件名称
//     * @param fullPath 文件全路径 ,比如  /tmp/vclound/email_file/test.jpg
//     * @return test.jpg
//     */
//    public static String getFileNameByFullPath(String fullPath){
//    	File tempFile = null;
//    	try{
//    		tempFile = new File(fullPath);
//            String fileName = tempFile.getName();
//            return fileName;
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}finally{
//    		if(tempFile !=null){
//    			tempFile = null;
//    		}
//    	}
//    	return null;
//    }
//
//
//    /**
//     * 发送带有图片的邮件(注:如果邮件正文中含有图片,html邮件正文必须包括图片的链接部分)
//     * @param emailSubject 邮件主题
//     * @param htmlMailContent html格式的邮件内容
//     * @param pictureFilePathSet  图片文件目录全路径集合,比如 /tmp/vclound/email_file/test.jpg
//     * @param receiverAddressSet 接收者地址集合
//     * @return true:成功   false:失败
//     * @throws Exception
//     */
//    public static boolean sendMultiPicturesMailHandler(String emailSubject, String htmlMailContent, Set<String> pictureFilePathSet, Set<String> receiverAddressSet)
//    		throws Exception {
//		return EmailUtil.sendMultiPicturesMailHandler(null, null, emailSubject, htmlMailContent, pictureFilePathSet, receiverAddressSet, null, null);
//	}
//
//    /**
//     * 发送带有图片的邮件
//     * @param sender 发送者邮件地址
//     * @param senderPwd 发送者密码
//     * @param emailSubject 邮件主题
//     * @param htmlMailContent html格式的邮件内容
//     * @param pictureFilePathSet  图片文件目录全路径集合,比如 /tmp/vclound/email_file/test.jpg
//     * @param receiverAddressSet 接收者地址集合
//     * @param ccAddressSet 抄送者地址集合（可空）
//     * @param bccAddressSet 暗送者地址集合（可空）
//     * @return true:成功   false:失败
//     * @throws Exception
//     */
//    public static boolean sendMultiPicturesMailHandler(String sender, String senderPwd, String emailSubject, String htmlMailContent, Set<String> pictureFilePathSet, Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
//    		throws Exception {
//		try{
//			if(null == pictureFilePathSet || pictureFilePathSet.isEmpty() ){
//
//			}
//			if(BlankUtil.isBlank(sender)){
//				sender = SMTP_AUTH_USER;
//			}
//			if(BlankUtil.isBlank(senderPwd)){
//				senderPwd = SMTP_AUTH_PWD;
//			}
//
//			Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
//			Properties props = new Properties();
//	        props.put("mail.transport.protocol", SMTP_PROTOCOL);
//	        props.put("mail.smtp.host", SMTP_HOST_NAME);
//	        props.put("mail.smtp.auth", "true");
//	        props.put("mail.smtp.port", SMTP_SERVER_PORT);
//	        if(ssl){
//	        	props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//	        }
//			// 获得邮件会话对象
//			Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
//			/** *************************************************** */
//			// 创建MIME邮件对象
//			MimeMessage mimeMessage = new MimeMessage(session);
//			mimeMessage.setFrom(new InternetAddress(sender));// 发件人
//			mimeMessage.setRecipients(Message.RecipientType.TO, receivers);// 收件人
//			if(null != ccAddressSet && !ccAddressSet.isEmpty()){
//				Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);  //抄送
//			}
//			if(null != bccAddressSet && !bccAddressSet.isEmpty()){
//				Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);  //暗送
//			}
//			mimeMessage.setSubject(emailSubject, CHARSET_UTF8); //防止中文乱码
//			mimeMessage.setSentDate(new Date());// 发送日期
//
//			//邮件正文处理:普通文本片段、附件片段、图片片段
//			Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件,related
//			//Multipart mp = new MimeMultipart("alternative"); //混合片段,都可用
//
//			//html正文：
//			BodyPart bodyPart = new MimeBodyPart();// 正文
//			bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");  //html片段
//			mp.addBodyPart(bodyPart);
//
//			int i = 1;
//			for(String pictureFilePath : pictureFilePathSet){
//				// 图片
//				String fileName =  EmailUtil.getFileNameByFullPath(pictureFilePath);
//				MimeBodyPart imgBodyPart = new MimeBodyPart(); // 附件图标
//				byte[] bytes = readFile(pictureFilePath);  //"D:/soft/qy/desk_img/1.jpg"
//				ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
//				imgBodyPart.setDataHandler(new DataHandler(fileds));
//				imgBodyPart.setFileName(fileName);
//				imgBodyPart.setHeader("Content-ID", "<IMG"+i+">"); // !!!!注意这里是"<IMG1>" 带有尖括号 而在正文的html里面则是src="cid:IMG1"
//				mp.addBodyPart(imgBodyPart);
//
//				i++;
//			}
//
//			mimeMessage.setContent(mp);// 设置邮件内容对象
//			mimeMessage.saveChanges();
//			Transport.send(mimeMessage);// 发送邮件
//		}catch(Exception  e){
//			e.printStackTrace();
//			throw e;
//		}
//		return true;
//	}
//
//
//    /**
//     * 发送带有图片的邮件
//     * @param sender 发送者邮件地址
//     * @param senderPwd 发送者密码
//     * @param emailSubject 邮件主题
//     * @param htmlMailContent html格式的邮件内容
//     * @param attachFilePathSet 多个附加全路径的集合
//     * @param pictureFilePathSet  图片文件目录全路径集合,比如 /tmp/vclound/email_file/test.jpg
//     * @param receiverAddressSet 接收者地址集合
//     * @param ccAddressSet 抄送者地址集合（可空）
//     * @param bccAddressSet 暗送者地址集合（可空）
//     * @return true:成功   false:失败
//     * @throws Exception
//     */
//    public static boolean sendMultiAttachsMailHandler(String sender, String senderPwd, String emailSubject,
//    		String htmlMailContent, Set<String> attachFilePathSet, Set<String> pictureFilePathSet,
//    		Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
//    		throws Exception {
//		try{
//			if(BlankUtil.isBlank(sender)){
//				sender = SMTP_AUTH_USER;
//			}
//			if(BlankUtil.isBlank(senderPwd)){
//				senderPwd = SMTP_AUTH_PWD;
//			}
//
//			Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
//			Properties props = new Properties();
//	        props.put("mail.transport.protocol", SMTP_PROTOCOL);
//	        props.put("mail.smtp.host", SMTP_HOST_NAME);
//	        props.put("mail.smtp.auth", "true");
//	        props.put("mail.smtp.port", SMTP_SERVER_PORT);
//	        if(ssl){
//	        	props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//	        }
//			// 获得邮件会话对象
//			Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
//			/** *************************************************** */
//			// 创建MIME邮件对象
//			MimeMessage mimeMessage = new MimeMessage(session);
//			mimeMessage.setFrom(new InternetAddress(sender));// 发件人
//			mimeMessage.setRecipients(Message.RecipientType.TO, receivers);// 收件人
//			if(null != ccAddressSet && !ccAddressSet.isEmpty()){
//				Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);  //抄送
//			}
//			if(null != bccAddressSet && !bccAddressSet.isEmpty()){
//				Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);  //暗送
//			}
//			mimeMessage.setSubject(emailSubject, CHARSET_UTF8); //防止中文乱码
//			mimeMessage.setSentDate(new Date());// 发送日期
//
//			//邮件正文处理:普通文本片段、附件片段、图片片段
//			Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件,related
//			//Multipart mp = new MimeMultipart("alternative"); //混合片段,都可用
//
//			//html正文：
//			BodyPart bodyPart = new MimeBodyPart();// 正文
//			bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");  //html片段
//			mp.addBodyPart(bodyPart);
//
//			int i = 1;
//			for(String pictureFilePath : pictureFilePathSet){
//				// 图片
//				String fileName =  EmailUtil.getFileNameByFullPath(pictureFilePath);
//				MimeBodyPart imgBodyPart = new MimeBodyPart(); // 附件图标
//				byte[] bytes = readFile(pictureFilePath);  //"D:/soft/qy/desk_img/1.jpg"
//				ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
//				imgBodyPart.setDataHandler(new DataHandler(fileds));
//				imgBodyPart.setFileName(fileName);
//				imgBodyPart.setHeader("Content-ID", "<IMG"+i+">"); // !!!!注意这里是"<IMG1>" 带有尖括号 而在正文的html里面则是src="cid:IMG1"
//				mp.addBodyPart(imgBodyPart);
//
//				i++;
//			}
//
//			mimeMessage.setContent(mp);// 设置邮件内容对象
//			mimeMessage.saveChanges();
//			Transport.send(mimeMessage);// 发送邮件
//		}catch(Exception  e){
//			e.printStackTrace();
//			throw e;
//		}
//		return true;
//	}
//
//    /**
//     * 发送带有图片的邮件
//     * @param sender 发送者邮件地址
//     * @param senderPwd 发送者密码
//     * @param emailSubject 邮件主题
//     * @param htmlMailContent html格式的邮件内容
//     * @param pictureFilePath  图片文件目录全路,比如 /tmp/vclound/email_file/test.jpg
//     * @param receiverAddressSet 接收者地址集合
//     * @param ccAddressSet 抄送者地址集合（可空）
//     * @param bccAddressSet 暗送者地址集合（可空）
//     * @return true:成功   false:失败
//     * @throws Exception
//     */
//    public static boolean sendPictureMailHandler(String sender, String senderPwd, String emailSubject, String htmlMailContent, String pictureFilePath, Set<String> receiverAddressSet, Set<String> ccAddressSet, Set<String> bccAddressSet)
//    		throws Exception {
//		try{
//			if(BlankUtil.isBlank(sender)){
//				sender = SMTP_AUTH_USER;
//			}
//			if(BlankUtil.isBlank(senderPwd)){
//				senderPwd = SMTP_AUTH_PWD;
//			}
//
//			Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
//			Properties props = new Properties();
//	        props.put("mail.transport.protocol", SMTP_PROTOCOL);
//	        props.put("mail.smtp.host", SMTP_HOST_NAME);
//	        props.put("mail.smtp.auth", "true");
//	        props.put("mail.smtp.port", SMTP_SERVER_PORT);
//	        if(ssl){
//	        	props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//	        }
//			// 获得邮件会话对象
//			Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
//			/** *************************************************** */
//			// 创建MIME邮件对象
//			MimeMessage mimeMessage = new MimeMessage(session);
//			mimeMessage.setFrom(new InternetAddress(sender));// 发件人
//			mimeMessage.setRecipients(Message.RecipientType.TO, receivers);// 收件人
//			if(null != ccAddressSet && !ccAddressSet.isEmpty()){
//				Address[] ccReceivers = EmailUtil.builderMultiReceiver(ccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.CC, ccReceivers);  //抄送
//			}
//			if(null != bccAddressSet && !bccAddressSet.isEmpty()){
//				Address[] bccReceivers = EmailUtil.builderMultiReceiver(bccAddressSet);
//				mimeMessage.setRecipients(Message.RecipientType.BCC, bccReceivers);  //暗送
//			}
//			mimeMessage.setSubject(emailSubject, CHARSET_UTF8); //防止中文乱码
//			mimeMessage.setSentDate(new Date());// 发送日期
//
//			//邮件正文处理:普通文本片段、附件片段、图片片段
//			Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件,related
//			//Multipart mp = new MimeMultipart("alternative"); //混合片段,都可用
//
//			//html正文：
//			BodyPart bodyPart = new MimeBodyPart();// 正文
//			bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");  //html片段
//			mp.addBodyPart(bodyPart);
//
//			// 图片
//			String fileName =  EmailUtil.getFileNameByFullPath(pictureFilePath);
//			MimeBodyPart imgBodyPart = new MimeBodyPart(); // 附件图标
//			byte[] bytes = readFile(pictureFilePath);  //"D:/soft/qy/desk_img/1.jpg"
//			ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
//			imgBodyPart.setDataHandler(new DataHandler(fileds));
//			imgBodyPart.setFileName(fileName);
//			imgBodyPart.setHeader("Content-ID", "<IMG1>"); // !!!!注意这里是"<IMG1>" 带有尖括号 而在正文的html里面则是src="cid:IMG1"
//			mp.addBodyPart(imgBodyPart);
//			/** *************************************************** */
//
//			mimeMessage.setContent(mp);// 设置邮件内容对象
//			mimeMessage.saveChanges();
//			Transport.send(mimeMessage);// 发送邮件
//		}catch(Exception  e){
//			e.printStackTrace();
//			throw e;
//		}
//		return true;
//	}
//
//    public static boolean sendMixMail(String sender, String senderPwd, String emailSubject, String painContent,
//    		String htmlMailContent, Set<String> receiverAddressSet)
//    		throws Exception {
//		try{
//			if(BlankUtil.isBlank(sender)){
//				sender = SMTP_AUTH_USER;
//			}
//			if(BlankUtil.isBlank(senderPwd)){
//				senderPwd = SMTP_AUTH_PWD;
//			}
//
//			Address[] receivers = EmailUtil.builderMultiReceiver(receiverAddressSet);
//			Properties props = new Properties();
//	        props.put("mail.transport.protocol", SMTP_PROTOCOL);
//	        props.put("mail.smtp.host", SMTP_HOST_NAME);
//	        props.put("mail.smtp.auth", "true");
//	        props.put("mail.smtp.port", SMTP_SERVER_PORT);
//
//	        if(ssl){
//	        	props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//	        }
//			// 获得邮件会话对象
//			Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(sender, senderPwd));
//			/** *************************************************** */
//			// 创建MIME邮件对象
//			MimeMessage mimeMessage = new MimeMessage(session);
//			mimeMessage.setFrom(new InternetAddress(sender));// 发件人
//			mimeMessage.setRecipients(Message.RecipientType.TO, receivers);// 收件人
//			//mimeMessage.setRecipients(Message.RecipientType.CC, receivers);  //抄送
//			//mimeMessage.setRecipients(Message.RecipientType.BCC, receivers); //暗送
//			mimeMessage.setSubject(emailSubject, CHARSET_UTF8); //防止中文乱码
//			mimeMessage.setSentDate(new Date());// 发送日期
//
//			//邮件正文处理:普通文本片段、附件片段、图片片段
//			//Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
//			Multipart mp = new MimeMultipart("alternative"); //混合片段,都可用
//
//			/** *************************************************** */
//			BodyPart bodyPart = new MimeBodyPart();// 正文
//			bodyPart.setText(painContent);  //纯文本内容
//			bodyPart.setDataHandler(new DataHandler("测<img src='http://a.hiphotos.baidu.com/image/pic/item/e7cd7b899e510fb3a78c787fdd33c895d0430c44.jpg' />试",
//					"text/html;charset=UTF-8"));// 网页格式
//
//			bodyPart.setContent(htmlMailContent, "text/html;charset=UTF-8");  //html片段
//			/** *************************************************** */
//			BodyPart attachBodyPart = new MimeBodyPart();// 普通附件
//			BodyPart attachBodyPart2 = new MimeBodyPart();// 普通附件
//
//
//			//FileDataSource fds = new FileDataSource("D:/不得不承认Zeroc-Ice是RPC王者完爆Dubbo-Thrift.docx");
//			FileDataSource fds = new FileDataSource("D:/麒云v1.0任务分派.pdf");  //附件
//			String fileName =  MimeUtility.encodeText(fds.getName());  //解决附件名称,中文乱码的问题   =?utf-8?Q?=E9=BA=92=E4=BA=91v1.0=E4=BB=BB=E5=8A=A1=E5=88=86=E6=B4=BE.pdf?=
//			attachBodyPart.setDataHandler(new DataHandler(fds));
//			//attachBodyPart.setFileName(new sun.misc.BASE64Encoder().encode(fds.getName().getBytes(CHARSET)));// 解决附件名中文乱
//			attachBodyPart.setFileName(fileName); // 解决附件名中文乱
//			mp.addBodyPart(attachBodyPart);
//
//			FileDataSource fds2 = new FileDataSource("D:/nginx-qy-default.rar");  //附件
//			attachBodyPart2.setDataHandler(new DataHandler(fds2));
//			attachBodyPart2.setFileName(MimeUtility.encodeText(fds2.getName()));
//			mp.addBodyPart(attachBodyPart2);
//
//			/** *************************************************** */
//			MimeBodyPart imgBodyPart = new MimeBodyPart(); // 附件图标
//			byte[] bytes = readFile("D:/soft/qy/desk_img/1.jpg");
//			ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
//			imgBodyPart.setDataHandler(new DataHandler(fileds));
//			imgBodyPart.setFileName("1.jpg");
//			//imgBodyPart.setHeader("Content-ID", "<img1></img1>");// 在html中使用该图片方法src="cid:IMG1"
//			imgBodyPart.setHeader("Content-ID", "<image1>");//
//			mp.addBodyPart(imgBodyPart);
//			/** *************************************************** */
//			System.out.println("3333333");
//			mp.addBodyPart(bodyPart);
//			mimeMessage.setContent(mp);// 设置邮件内容对象
//			Transport.send(mimeMessage);// 发送邮件
//			System.out.println("44444444");
//		}catch(Exception  e){
//			e.printStackTrace();
//		}
//		System.out.println("over~");
//		return true;
//	}
//
//
//	public static void main(String[] args) throws Exception {
//		Set<String> receiverAddressSet = new HashSet<String>();
//		receiverAddressSet.add("778521003@qq.com");
//		receiverAddressSet.add("1640317717@qq.com");
//
//		Set<String> ccAddressSet = new HashSet<String>();
//
//		Set<String> bccAddressSet = new HashSet<String>();
//
//
//		String emailHtmlContent = "";
//		emailHtmlContent += "<table width=\"700\" border=\"1\" style=\"font-size:12px\">";
//         emailHtmlContent += "<tr>";
//         emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>序号</td>";
//         emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>项目</td>";
//         emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>主机名称</td>";
//         emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>监控类型</td>";
//         emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>监控</td>";
//         emailHtmlContent += "<td bgcolor='#666666' style='color: #FFF'>监控</td>";
//         emailHtmlContent += "</tr>";
//         emailHtmlContent += "</table>";
//         emailHtmlContent +=          EmailUtil.makeMailSignatureDefault();
//
//         String pictureFilePath = "F:/system/timg.jpg";
//         String htmlMailContent = "<b>Some <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG1\"><br>good!";
//         EmailUtil.sendHtmlMailDefault("测试邮件发送888", emailHtmlContent, receiverAddressSet);
//        //EmailUtil.sendPictureMailHandler(null, null, "测试图片邮件发送888", htmlMailContent, pictureFilePath, receiverAddressSet, null, null);
//
//         Set<String> pictureFilePathSet = new HashSet<String>();
//         pictureFilePathSet.add("F:/system/1.jpg");
//         pictureFilePathSet.add("F:/system/美女.jpg");
//         htmlMailContent = "<b>第一张 Some <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG1\"><br>good!";
//         htmlMailContent += "<br><b>第二张 <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG2\"><br>good!";
//         EmailUtil.sendMultiPicturesMailHandler("", null, "男人看了会沉默，女人看了会流泪。", htmlMailContent, pictureFilePathSet, receiverAddressSet, ccAddressSet, bccAddressSet);
//	}
//
//	/**
//	 * 读取文件内容
//	 * @param file 文件的全路径名,比如  D:/soft/qy/desk_img/1.jpg
//	 * @return 返回文件内容的二进制字节数组
//	 */
//	public static byte[] readFile(String file) {
//		FileInputStream fis = null;
//		ByteArrayOutputStream bos = null;
//		try {
//			fis = new FileInputStream(file);
//			bos = new ByteArrayOutputStream();
//			int bytesRead;
//			byte buffer[] = new byte[1024 * 1024];
//			while ((bytesRead = fis.read(buffer)) != -1) {
//				bos.write(buffer, 0, bytesRead);
//				Arrays.fill(buffer, (byte) 0);
//			}
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		} finally {
//			try {
//				if (bos != null)
//					bos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return bos.toByteArray();
//	}
//}
//
///**
// * Smtp认证
// */
//class SmtpAuthenticator extends Authenticator {
//	String username = null;
//	String password = null;
//
//	// SMTP身份验证
//	public SmtpAuthenticator(String username, String password) {
//		this.username = username;
//		this.password = password;
//	}
//
//	public PasswordAuthentication getPasswordAuthentication() {
//		return new PasswordAuthentication(this.username, this.password);
//	}
//}
//
//
//class ByteArrayDataSource implements DataSource {
//	private final String contentType;
//	private final byte[] buf;
//	private final int len;
//
//	public ByteArrayDataSource(byte[] buf, String contentType) {
//		this(buf, buf.length, contentType);
//	}
//
//	public ByteArrayDataSource(byte[] buf, int length, String contentType) {
//		this.buf = buf;
//		this.len = length;
//		this.contentType = contentType;
//	}
//
//	public String getContentType() {
//		if (contentType == null){
//			return "application/octet-stream";
//		}
//		return contentType;
//	}
//
//	public InputStream getInputStream() {
//		return new ByteArrayInputStream(buf, 0, len);
//	}
//
//	public String getName() {
//		return null;
//	}
//
//	public OutputStream getOutputStream() {
//		throw new UnsupportedOperationException();
//	}
//}
