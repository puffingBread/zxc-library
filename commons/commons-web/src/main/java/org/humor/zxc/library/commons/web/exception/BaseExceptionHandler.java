package org.humor.zxc.library.commons.web.exception;

import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.apache.commons.lang3.StringUtils;
import org.humor.zxc.library.commons.web.dto.ResultWrapper;
import org.humor.zxc.library.commons.web.utils.ResultWrapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order
@ConditionalOnWebApplication
public class BaseExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);
    @Resource
    private MessageSource messageSource;

    @PostConstruct
    public void init() {
        logger.info("-//(ǒ.ǒ)//- BaseExceptionHandler");
    }

    /**
     * 参数格式异常，多发生于联调阶段
     *
     * @param e      异常
     * @param locale 地理信息
     * @return 错误信息
     */
    @ExceptionHandler({JSONException.class, JsonParseException.class, HttpMessageNotReadableException.class,
            HttpMessageConversionException.class})
    public ResultWrapper<?> jsonExceptionHandler(Exception e, Locale locale) {
        logger.warn(e.getMessage(), e);
        ResultWrapper.Error error = ResultWrapperUtil.buildError("illegal.args", locale);
        if (e.getCause() != null && e.getCause() instanceof MismatchedInputException) {

            MismatchedInputException exception = (MismatchedInputException) e.getCause();

            exception.getPath().stream().filter(n -> n.getFieldName() != null)
                    .forEach(n -> error.addDetails(ResultWrapperUtil.buildError(
                            "illegal.args.format", n.getFieldName(), locale)));
        }

        return ResultWrapper.badRequest(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultWrapper<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, Locale locale) {
        ResultWrapper.Error error = ResultWrapperUtil.buildError("illegal.args", e.getName(), locale);
        return ResultWrapper.badRequest(error);
    }

    /**
     * 接口参数校验异常
     *
     * @param e BindException or MethodArgumentNotValidException
     * @return 错误保证类
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResultWrapper<?> argumentNotValidException(Exception e, Locale locale) {
        logger.warn(e.getMessage(), e);
        BindingResult bindingResult = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();

        List<ResultWrapper.Error> details = new ArrayList<>();
        bindingResult.getAllErrors().forEach(n -> {
            details.add(new ResultWrapper.Error(n.getCode(), n.getDefaultMessage(),
                    n instanceof FieldError ? ((FieldError) n).getField() : n.getObjectName()));
        });

        ResultWrapper.Error error =
                ResultWrapperUtil.buildError("illegal.args", locale);
        error.setDetails(details);

        String errMsg = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return ResultWrapper.badRequest(error, errMsg);
    }

    /**
     * 服务端异常
     * Validation 校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultWrapper<?> handleConstraintViolationException(ConstraintViolationException e, Locale locale, ContentCachingRequestWrapper request) {

        if (request != null) {
            String errMsg = String.format("url=%s?%s;payload=%s;header=%s", request.getRequestURI(), request.getQueryString(),
                    StringUtils.toEncodedString(request.getContentAsByteArray(), Charset.forName(request.getCharacterEncoding())),
                    new ServletServerHttpRequest(request).getHeaders());
            logger.warn("business.check.incorrect: {}", errMsg, e);
        } else {
            logger.warn("business.check.incorrect", e);
        }

        return ResultWrapperUtil.fail("unknown.server.exception");
    }

//    @ExceptionHandler(MobDecryptException.class)
//    public ResultWrapper<String> mobEncryptException(MobDecryptException e) {
//        log.warn(e.getCode(), e);
//        return ResultWrapper.failMsg("解密失败");
//    }

    /**
     * @param e 业务线统一异常
     */
    @ExceptionHandler(ReadMessageException.class)
    public ResultWrapper<?> mobException(ReadMessageException e) {
        logger.warn(e.getMessage(), e);
        if (StringUtils.isEmpty(e.getMessage())) {
            return ResultWrapperUtil.fail(e.getCode());
        }
        return ResultWrapperUtil.failMsg(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ArgException.class)
    public ResultWrapper<?> mobArgException(ArgException e, Locale locale) {
        logger.warn(e.getMessage(), e);
        ResultWrapper.Error error =
                ResultWrapperUtil.buildError("illegal.args", locale);
        error.setDetails(Collections.singletonList(ResultWrapperUtil.buildError(e.getCode(), locale)));
        return ResultWrapper.badRequest(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResultWrapper<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, Locale locale) {
        logger.warn(e.getMessage(), e);
        return ResultWrapperUtil.fail("max.upload.size.exceeded");
    }

    @ExceptionHandler(ServletException.class)
    public ResultWrapper<String> exceptionHandler(ServletException e) {
        logger.warn(e.getMessage(), e);
        return ResultWrapper.failMsg(e.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResultWrapper<String> missingServletRequestParameterException(ServletException e) {
        logger.warn(e.getMessage(), e);
        return ResultWrapper.failMsg(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultWrapper<String> exception(Exception e, ContentCachingRequestWrapper request) {

        if (e.getCause() != null && e.getCause() instanceof ReadMessageException) {
            ReadMessageException cause = (ReadMessageException) e.getCause();
            if (StringUtils.isEmpty(cause.getMessage())) {
                return ResultWrapperUtil.fail(cause.getCode());
            }
            return ResultWrapperUtil.fail(cause.getCode(), cause.getMessage());
        }
//        if (request != null) {
//            String errMsg = String.format("url=%s?%s;payload=%s;header=%s", request.getRequestURI(), request.getQueryString(),
//                    StringUtils.toEncodedString(request.getContentAsByteArray(), Charset.forName(request.getCharacterEncoding())),
//                    new ServletServerHttpRequest(request).getHeaders());
//            MailLogger.logError("unknown.server.exception", errMsg, e);
//        } else {
//            MailLogger.logError("unknown.server.exception", e);
//        }
        return ResultWrapperUtil.fail("unknown.server.exception");
    }
}
