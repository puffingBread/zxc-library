package org.humor.zxc.library.commons.util.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class OkHttpUtils {

    private static final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType xmlMediaType = MediaType.parse("application/xml; charset=utf-8");

    private final OkHttpClient okHttpClient;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return execute(request);
    }

    public String get(String url, Map<String, String> headersMap) {
        if (Objects.isNull(headersMap)) {
            return get(url);
        }
        Request request = new Request.Builder()
                .headers(Headers.of(headersMap))
                .url(url)
                .get()
                .build();
        return execute(request);
    }

    public String post(String url) {
        Headers headers = Headers.of("token", "");

        RequestBody requestBody = RequestBody.create("", MediaType.get(""));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execute(request);
    }

    public String post(String url, String body) {
        RequestBody requestBody = RequestBody.create(body, jsonMediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execute(request);
    }

    public String post(String url, Map<String, String> headersMap, String body) {
        RequestBody requestBody = RequestBody.create(body, jsonMediaType);
        Request request = new Request.Builder()
                .headers(Headers.of(headersMap))
                .url(url)
                .post(requestBody)
                .build();
        return execute(request);
    }

    public String execute(Request request) {
        Call call = okHttpClient.newCall(request);
        String responseBody;
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            responseBody = Objects.isNull(body) ? null : body.string();
            log.info("execute get success={}, response={}", response.isSuccessful(), JSON.toJSONString(response));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("execute get request error={}", e.getMessage());
            return null;
        }
        return responseBody;
    }
}
