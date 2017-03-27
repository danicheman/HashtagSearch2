package com.actest.nick.hashtagsearch2.data;

import com.actest.nick.hashtagsearch2.SearchApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Adapted From:
 *
 * @link https://github.com/adavis/adept-android
 */
public class UseCacheWhenOfflineInterceptor implements Interceptor {

    private static final String CACHE_CONTROL = "Cache-Control";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Add Cache Control only for GET methods
        if (request.method().equals("GET")) {
            if (!SearchApplication.isConnectedToInternet()) {
                // 1 week stale
                request = request.newBuilder()
                        .header(CACHE_CONTROL, "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                        .build();
            }
        }

        return chain.proceed(request);
    }
}
