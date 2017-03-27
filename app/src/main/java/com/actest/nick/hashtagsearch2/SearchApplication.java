package com.actest.nick.hashtagsearch2;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.actest.nick.hashtagsearch2.cache.UseCacheWhenOfflineInterceptor;
import com.actest.nick.hashtagsearch2.cache.AlwaysSaveToCacheInterceptor;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;


import java.io.File;

import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class SearchApplication extends Application {

    private static SearchApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        long sizeOfCache = 10 * 1024 * 1024;
        Cache cache = new Cache(new File(getCacheDir(), "http"), sizeOfCache);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor( new UseCacheWhenOfflineInterceptor())
                .addNetworkInterceptor(new AlwaysSaveToCacheInterceptor())
                .build();

        TwitterCore.getInstance().addGuestApiClient(new TwitterApiClient(client));
    }

    public static boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager)instance.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }
}
