package com.naman14.hacktoberfest.network.api;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * Created by naman on 17/6/17.
 */

public class ApiInterceptor implements Interceptor {

    private static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private String authToken;


    public ApiInterceptor(String authToken) {
        this.authToken = authToken;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Builder builder = chainRequest.newBuilder();
        builder.header(HEADER_ACCEPT, "application/vnd.github.v3+json");
        builder.header(HEADER_USER_AGENT, "Hacktoberfest-Android-App");
        if (!TextUtils.isEmpty(authToken)) {
            builder.header(HEADER_AUTH, "Bearer " + authToken);
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
