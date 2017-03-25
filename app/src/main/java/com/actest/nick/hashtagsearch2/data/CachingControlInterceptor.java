package com.actest.nick.hashtagsearch2.data;

import android.support.annotation.NonNull;

import com.actest.nick.hashtagsearch2.SearchApplication;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by NICK on 3/25/2017.
 */

public class CachingControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder request = originalRequest.newBuilder();
        if (originalRequest.header("fresh") != null) {
            request.cacheControl(CacheControl.FORCE_NETWORK);
        }
        Response response = chain.proceed(request.build());
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl())
                .build();
    }

    @NonNull
    private String cacheControl() {
        String cacheHeaderValue;
        if (SearchApplication.isConnectedToInternet()) {
            cacheHeaderValue = "public, max-age=2419200";
        } else {
            cacheHeaderValue = "public, only-if-cached, max-stale=2419200";
        }
        return cacheHeaderValue;
    }
}