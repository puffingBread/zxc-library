package org.humor.zxc.library.starter.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Smtp认证
 * Date: 2021/12/4
 * Time: 5:05 PM
 *
 * @author xuzz
 */
public class SmtpAuthenticator extends Authenticator {
    String username = null;
    String password = null;

    // SMTP身份验证
    public SmtpAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username, this.password);
    }
}

