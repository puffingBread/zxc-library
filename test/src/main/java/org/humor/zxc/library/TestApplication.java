package org.humor.zxc.library;

import okhttp3.OkHttpClient;
import org.humor.zxc.library.commons.util.utils.OkHttpUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public OkHttpUtils okHttpUtils() {
        return new OkHttpUtils(new OkHttpClient());
    }
}
