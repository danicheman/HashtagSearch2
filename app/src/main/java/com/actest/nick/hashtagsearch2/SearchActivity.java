package com.actest.nick.hashtagsearch2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.actest.nick.hashtagsearch2.data.SearchHistorySaver;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean>, SearchHistoryFragment.SearchHistoryAccessibleInterface {

    private static final String TAG = "SearchActivity";
    private static final String SEARCH_HISTORY_FILENAME = "SearchHistory";
    private final String SEARCH_FRAGMENT_NAME = "SearchFragment";

    // todo: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "V6eNOeMpu5mO1srwlPFNX1GDT";
    private static final String TWITTER_SECRET = "uah6slziKgGHZWMiSEUNrRxlu72n1qnS4qartJbT2Vnto2WUGz";

    private Toolbar toolbar;
    private ArrayList<String> mSearchHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new SearchHistoryFragment())
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "handleIntent: query:" + query);

            SearchResultsFragment searchResultsFragment;

            //check the current fragment type
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

            if(!mSearchHistory.contains(query)) {
                mSearchHistory.add(query);
                //todo: fix loader - http://stackoverflow.com/questions/10524667/android-asynctaskloader-doesnt-start-loadinbackground
                getSupportLoaderManager().initLoader(0, null, this).forceLoad();
            }

            if (currentFragment instanceof SearchResultsFragment) {
                ((SearchResultsFragment) currentFragment).search(query);
            } else {
                searchResultsFragment = new SearchResultsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                searchResultsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, searchResultsFragment)
                        .addToBackStack(SEARCH_FRAGMENT_NAME)
                        .commitAllowingStateLoss();
            }
            Log.d(TAG, "onSearchRequested: " + query);


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity todo: searchableinfo
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        return new SearchHistorySaver(this, mSearchHistory, getSearchHistoryFilename());
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
        if (data) {
            Log.d(TAG, "onLoadFinished: Successfully Saved");
        } else {
            Log.e(TAG, "onLoadFinished: Error");
        }
    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }

    // Public functions for SearchHistoryFragment
    @Override
    public void setSearchHistory(ArrayList<String> searchHistory) {
        mSearchHistory = searchHistory;
    }

    @Override
    public String getSearchHistoryFilename() {
        return SEARCH_HISTORY_FILENAME;
    }
}
