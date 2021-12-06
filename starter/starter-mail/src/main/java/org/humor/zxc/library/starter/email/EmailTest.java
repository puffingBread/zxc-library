package org.humor.zxc.library.starter.email;

import java.util.HashSet;
import java.util.Set;

/**
 * 用java发送邮件的例子,支持：纯文本片段、html片段、附件、图片;
 */
public class EmailTest {

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
        emailHtmlContent += org.humor.zxc.library.commons.util.utils.EmailUtil.makeMailSignatureDefault();

        org.humor.zxc.library.commons.util.utils.EmailUtil.sendHtmlMailDefault("测试邮件发送888", emailHtmlContent, receiverAddressSet);

        String htmlMailContent = "<b>Some <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG1\"><br>good!";
        Set<String> pictureFilePathSet = new HashSet<>();
        pictureFilePathSet.add("/Users/Downloads/banner.png");
        pictureFilePathSet.add("/Users/Downloads/detail_banner.png");
//        EmailUtil.sendPictureMailHandler(null, null, "测试图片邮件发送888", htmlMailContent, pictureFilePath, receiverAddressSet, null, null);

        htmlMailContent += "<b>第一张 Some <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG1\"><br>good!";
        htmlMailContent += "<br><b>第二张 <i>HTML</i> text</b> and an image.<br><img src=\"cid:IMG2\"><br>good!";
        org.humor.zxc.library.commons.util.utils.EmailUtil.sendMultiPicturesMailHandler("", null, "男人看了会沉默，女人看了会流泪。", htmlMailContent, pictureFilePathSet, receiverAddressSet, ccAddressSet, bccAddressSet);
    }
}


