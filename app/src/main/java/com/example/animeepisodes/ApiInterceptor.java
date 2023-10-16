package com.example.animeepisodes;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {
    private final String apiKey;
    private final String apiHost;

    public ApiInterceptor(String apiKey, String apiHost) {
        this.apiKey = apiKey;
        this.apiHost = apiHost;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request modifiedRequest = originalRequest.newBuilder()
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", apiHost)
                .build();
        return chain.proceed(modifiedRequest);
    }
}
