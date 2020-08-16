package org.humor.zxc.library.commons.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.UUID;

@Component
@ConditionalOnWebApplication
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper =  wrapRequest(request);

        HttpServletResponse responseWrapper = response;
        if (LOGGER.isDebugEnabled()) {
            responseWrapper = wrapResponse(response);
        }

        filterChain.doFilter(requestWrapper, responseWrapper);

        afterRequest(requestWrapper, responseWrapper);

    }

    private void afterRequest(ContentCachingRequestWrapper request, HttpServletResponse response)
        throws IOException {
        if (LOGGER.isDebugEnabled()) {

            logRequestBody(request, request.getRemoteAddr() + "|>");

            ContentCachingResponseWrapper responseWrapper = wrapResponse(response);
            logResponse(responseWrapper, request.getRemoteAddr() + "|<");
            responseWrapper.copyBodyToResponse();
        }
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
        StringBuilder msg = new StringBuilder();

        msg.append("uri=").append(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }

        String payload;
        byte[] buf = request.getContentAsByteArray();
        try {
            payload = new String(buf, request.getCharacterEncoding());
        } catch (IOException e) {
            payload = "[unknown]";
        }
        msg.append(";payload=").append(payload);
        msg.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());

        logContent(msg.toString().getBytes(), request.getCharacterEncoding(), prefix);
    }

    private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
        int status = response.getStatus();

        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getCharacterEncoding(),
                    StringUtils.join(Arrays.asList(prefix, status, HttpStatus.valueOf(status).getReasonPhrase()), " "));
        }
    }

    private static void logContent(byte[] content, String contentEncoding, String prefix) {
        if (!LOGGER.isDebugEnabled()) {
            return;
        }
        // 日志trade
        MDC.clear();
        MDC.put("request_id", UUID.randomUUID().toString().replaceAll("-", ""));

        try {
            String contentString = new String(content, contentEncoding);
            LOGGER.debug("{} {}", prefix, contentString);
        } catch (UnsupportedEncodingException e) {
            LOGGER.debug("{} [{} bytes content]", prefix, content.length);
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
