package com.actest.nick.hashtagsearch2;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.actest.nick.hashtagsearch2.data.UseCacheWhenOfflineInterceptor;
import com.actest.nick.hashtagsearch2.data.AlwaysSaveToCacheInterceptor;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;


import java.io.File;

import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class SearchApplication extends Application {


    // todo: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "V6eNOeMpu5mO1srwlPFNX1GDT";
    private static final String TWITTER_SECRET = "uah6slziKgGHZWMiSEUNrRxlu72n1qnS4qartJbT2Vnto2WUGz";

    private final long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB

    private static SearchApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        Cache cache = new Cache(new File(getCacheDir(), "http"), SIZE_OF_CACHE);

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
