package com.actest.nick.hashtagsearch2.data;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Adapted from:
 *
 * @link https://github.com/adavis/adept-android
 */
public class AlwaysSaveToCacheInterceptor implements Interceptor {

    private static final String CACHE_CONTROL = "Cache-Control";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        // re-write response header to force use of cache
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader(CACHE_CONTROL)
                .header(CACHE_CONTROL, "public, max-age=" + 60 * 2) // 2 minutes
                .build();
    }
}
