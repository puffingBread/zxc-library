package org.humor.zxc.library.commons.dao.config;

import org.humor.zxc.library.commons.dao.interceptor.SqlLogInterceptor;
import org.humor.zxc.library.commons.dao.translator.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 *  Date: 2019/9/19
 *  Time: 15:06
 *  @author xuzz
 */
@Configuration
public class InterceptorConfiguration {

    @Bean
    public TranslatorService insertTranslator() {
        return new InsertTranslatorImpl();
    }

    @Bean
    public TranslatorService updateTranslator() {
        return new UpdateTranslatorImpl();
    }

    @Bean
    public TranslatorFactory translatorFactory(TranslatorService insertTranslator, TranslatorService updateTranslator) {
        return new TranslatorFactory(insertTranslator, updateTranslator);
    }

    @Bean
    public Translator translator(TranslatorFactory translatorFactory) {
        return new Translator(translatorFactory);
    }


    /**
     * 操作记录Mybatis插件
     * 使用{@link ApplicationEventPublisher}发送相关时间，
     * 消费方需要实现相关
     * @return Mybatis 插件
     */
    @Bean
    public SqlLogInterceptor operatingInterceptor() {
        return new SqlLogInterceptor();
    }
}
