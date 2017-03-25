package com.actest.nick.hashtagsearch2;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.actest.nick.hashtagsearch2.data.CachingControlInterceptor;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.File;

import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class SearchApplication extends Application {

    // todo: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "V6eNOeMpu5mO1srwlPFNX1GDT";
    private static final String TWITTER_SECRET = "uah6slziKgGHZWMiSEUNrRxlu72n1qnS4qartJbT2Vnto2WUGz";

    private static SearchApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File(getCacheDir(), "http"), SIZE_OF_CACHE);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new CachingControlInterceptor())
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
