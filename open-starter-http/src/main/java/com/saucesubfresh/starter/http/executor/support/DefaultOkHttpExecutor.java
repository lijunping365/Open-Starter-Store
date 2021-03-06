package com.saucesubfresh.starter.http.executor.support;


import com.saucesubfresh.starter.http.exception.HttpException;
import com.saucesubfresh.starter.http.executor.AbstractHttpExecutor;
import com.saucesubfresh.starter.http.executor.OkHttpExecutor;
import com.saucesubfresh.starter.http.request.HttpRequest;
import com.saucesubfresh.starter.http.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Map;
import java.util.Objects;

/**
 * Created by lijunping
 **/
@Slf4j
public class DefaultOkHttpExecutor extends AbstractHttpExecutor implements OkHttpExecutor {

    private final OkHttpClient client;

    public DefaultOkHttpExecutor(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public String doGet(HttpRequest httpRequest) throws HttpException {
        StringBuffer sb = getQueryString(httpRequest.getUrl(),httpRequest.getParams());
        Request request = new Request.Builder()
                .url(sb.toString())
                .headers(Headers.of(httpRequest.getHeaders()))
                .build();
        return execNewCall(request);
    }

    @Override
    public String doPost(HttpRequest httpRequest) throws HttpException{
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = FormBody.create(JSON.toJSON(httpRequest.getParams()), mediaType);
        Request request = new Request.Builder()
                .url(httpRequest.getUrl())
                .headers(Headers.of(httpRequest.getHeaders()))
                .post(body)
                .build();
        return execNewCall(request);
    }

    private String execNewCall(Request request) throws HttpException{
        try (Response response = client.newCall(request).execute()) {
            return Objects.nonNull(response.body()) && response.isSuccessful() ? response.body().string() : null;
        } catch (Exception e) {
            log.error("okhttp3 execute error >> ex = {}", e.getMessage());
            throw new HttpException(e.getMessage());
        }
    }

    private StringBuffer getQueryString(String url, Map<String,String> queries){
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return sb;
    }
}