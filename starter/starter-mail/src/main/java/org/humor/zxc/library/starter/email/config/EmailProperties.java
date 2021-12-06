package org.humor.zxc.library.starter.email.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Date: 2021/12/4
 * Time: 5:22 PM
 *
 * @author xuzz
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "smtp")
public class EmailProperties {

    private int serverPort = 25;
    private String protocol = "smtp";
    private String host = "smtp.xxx.com";
    private String authUser = "humor@xxx.com";
    private String authPassword = "********";
    private Boolean ssl = false;

}


