package org.humor.zxc.library.commons.web.utils;

import org.humor.zxc.library.commons.web.dto.ResultWrapper;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

@Component
public class ResultWrapperUtil {

    private static ReloadableResourceBundleMessageSource messageSource;

    public static <T> ResultWrapper<T> fail(String errCode) {
        String errMsg = messageSource.getMessage(errCode, null, Locale.CHINA);
        return fail(errCode, errMsg);
    }

    public static <T> ResultWrapper<T> fail(String errCode, String errmsg) {
        ResultWrapper.Error error = buildError(errCode, errmsg);
        return ResultWrapper.fail(error, errmsg);
    }

    public static ResultWrapper failMsg(String errCode, String msg) {
        ResultWrapper.Error error = buildError(errCode, msg);

        return ResultWrapper.fail(error, msg);
    }

    public static ResultWrapper.Error buildError(String errCode, Locale locale) {
        return buildError(errCode, null, locale);
    }

    public static ResultWrapper.Error buildError(String errCode, String target, Locale locale) {
        String errMsg = messageSource.getMessage(errCode, null, locale);
        return new ResultWrapper.Error(errCode, errMsg, target);
    }


    public static ResultWrapper.Error buildError(String errCode, String message) {
        return new ResultWrapper.Error(errCode, message, null);
    }

    @Resource
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
        ResultWrapperUtil.messageSource = messageSource;
    }
}
