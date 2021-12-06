package org.humor.zxc.library.starter.email;

import java.util.Set;

/**
 * Date: 2021/12/4
 * Time: 5:45 PM
 *
 * @author xuzz
 */
public class EmailApi {

    public boolean sendHtmlMailDefault(String emailSubject, String htmlMailContent, Set<String> receiverAddressSet) {

        return false;
    }

    public static boolean sendHtmlMail(String sender, String senderPwd, String emailSubject,
                                       String htmlMailContent, Set<String> receiverAddressSet) {

        return false;
    }

    public static boolean sendHtmlMailHandler(String sender, String senderPwd, String emailSubject,
                                              String htmlMailContent, Set<String> receiverAddressSet,
                                              Set<String> ccAddressSet, Set<String> bccAddressSet) {

        return false;
    }
}
