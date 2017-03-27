package com.actest.nick.hashtagsearch2;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.Loader;
import android.view.View;

import com.actest.nick.hashtagsearch2.data.SearchHistorySaver;
import com.twitter.sdk.android.core.models.Search;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by NICK on 3/26/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class SearchActivityTest {

    private SearchActivity searchActivity;

    @Before
    public void setUp() {
        searchActivity = Robolectric.buildActivity(SearchActivity.class).create().start().resume().visible().get();
    }

    @Test
    public void litmus() {
        Drawable icon = searchActivity.getResources().getDrawable(R.mipmap.ic_launcher);
        assertNotNull("The application icon doesn't exist",icon);
    }

    @Test
    public void testActivityViewsExist() {
        View container = searchActivity.findViewById(R.id.toolbar_container);
        View toolbar = searchActivity.findViewById(R.id.toolbar);

        assertNotNull("The toolbar container does not exist", container);
        assertNotNull("The toolbar does not exist", toolbar);

    }

    @Test
    public void testFragmentViewExistsByDefault() {
        View searchHistory = searchActivity.findViewById(R.id.list_search_history);
        assertNotNull("The SearchHistoryFragment's search history list does not exist", searchHistory);
        SearchResultsFragment searchResultsFragment = (SearchResultsFragment) searchActivity.getSupportFragmentManager().findFragmentByTag(SearchResultsFragment.TAG);
        assertNull("SearchResultsFragment should't exist if we haven't performed a search", searchResultsFragment);
    }

    @Test
    public void loaderReturnsSearchHistorySaver() {
        Loader loader = searchActivity.onCreateLoader(1, null);
        assertThat("Search activity should use a search history saver", loader, CoreMatchers.instanceOf(SearchHistorySaver.class));
    }

    @Test
    public void testHandleIntentLoadsSearchResults() throws Exception {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, "a search");

        searchActivity.onNewIntent(intent);

        SearchResultsFragment searchResultsFragment = (SearchResultsFragment) searchActivity.getSupportFragmentManager().findFragmentByTag(SearchResultsFragment.TAG);
        assertNotNull("Fragment should exist after performing search", searchResultsFragment);

    }
}
