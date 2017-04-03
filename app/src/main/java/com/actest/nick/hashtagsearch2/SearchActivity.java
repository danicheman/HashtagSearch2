package com.actest.nick.hashtagsearch2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import io.fabric.sdk.android.services.common.CommonUtils;

public class SearchActivity extends AppCompatActivity implements SearchHistoryFragment.SearchHistoryAccessibleInterface, SearchView.OnFocusChangeListener,SearchView.OnCloseListener {

    private static final String TAG = "SearchActivity";
    private static final String SEARCH_HISTORY_FILENAME = "SearchHistory";

    static final String SEARCH_HISTORY_PREFS = "SearchHistoryPrefs";
    static final String SEARCH_HISTORY_KEY = "SearchHistory";

    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;
    private ArrayList<String> mSearchHistory = new ArrayList<>();
    private Toolbar toolbar;
    private Boolean showSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            mSearchMenuItem.collapseActionView();
            toolbar.setTitle(query);

            showSearch = false;
            invalidateOptionsMenu();
            SearchResultsFragment searchResultsFragment;

            //check the current fragment type
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

            if(!mSearchHistory.contains(query)) {
                mSearchHistory.add(query);
                updateSearchHistory();
            }

            if (currentFragment instanceof SearchResultsFragment) {
                ((SearchResultsFragment) currentFragment).search(query);
            } else {
                searchResultsFragment = new SearchResultsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                searchResultsFragment.setArguments(bundle);
                String SEARCH_FRAGMENT_NAME = "SearchFragment";
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, searchResultsFragment, SearchResultsFragment.TAG)
                        .addToBackStack(SEARCH_FRAGMENT_NAME)
                        .commitAllowingStateLoss();
            }
            Log.d(TAG, "onSearchRequested: " + query);
        }
    }

    private void updateSearchHistory() {
        getSharedPreferences(SEARCH_HISTORY_PREFS, MODE_PRIVATE)
                .edit()
                .putString(SEARCH_HISTORY_KEY,
                        new Gson().toJson(mSearchHistory)
                ).apply();
    }

    @Override
    public void onBackPressed() {
        toolbar.setTitle(getResources().getString(R.string.app_name));
        showSearch = true;
        invalidateOptionsMenu();
        super.onBackPressed();
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
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();

        mSearchMenuItem.setVisible(showSearch);

        if (showSearch) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconified(false); // Do not iconify the widget; expand it by default
        } else {

            CommonUtils.hideKeyboard(this, this.findViewById(android.R.id.content));
        }

        return true;
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.e(TAG, "FOCUS: asdfasdfsad!!");
    }

    @Override
    public boolean onClose() {
        Log.e(TAG, "CLOSE: asdfasdfsad!!");
        return false;
    }
}
