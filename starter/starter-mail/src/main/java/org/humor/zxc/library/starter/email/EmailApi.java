package org.humor.zxc.library.starter.email;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Date: 2021/12/4
 * Time: 5:45 PM
 *
 * @author xuzz
 */
@Component
public class EmailApi {

    @Resource
    private EmailSender emailSender;

    /**
     * 通过默认账号，发送html格式的邮件
     *
     * @param emailSubject       邮件主题
     * @param htmlMailContent    html格式的邮件内容
     * @param receiverAddressSet 接收者地址集合
     * @return true:成功   false:失败
     */
    public boolean sendHtmlMailDefault(String emailSubject, String htmlMailContent, Set<String> receiverAddressSet) {
        return emailSender.sendMailHandler(null, null, emailSubject, htmlMailContent, null, receiverAddressSet, null, null);
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
    public boolean sendHtmlMail(String sender, String senderPwd, String emailSubject, String htmlMailContent, Set<String> receiverAddressSet) {
        return emailSender.sendMailHandler(sender, senderPwd, emailSubject, htmlMailContent, null, receiverAddressSet, null, null);
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
    public boolean sendMultiPicturesMailHandler(String emailSubject, String htmlMailContent, Set<String> pictureFilePathSet, Set<String> receiverAddressSet) {
        return emailSender.sendMailHandler(null, null, emailSubject, htmlMailContent, pictureFilePathSet, receiverAddressSet, null, null);
    }
}
