package com.actest.nick.hashtagsearch2;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * A fragment representing a list of tweets matching the search

 */
public class SearchResultsFragment extends Fragment {

    public static final String TAG = "SearchResultsFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private TweetTimelineListAdapter adapter;

    private Handler searchRefreshHandler = new Handler();
    private int refreshInterval = 10000; //10 seconds

    private Runnable runnable;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchResultsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        String query = bundle.getString("query");

        updateRefreshIntervalFromSharedPreferences();
        if(query != null) {
            search(query);
        }
    }

    private void updateRefreshIntervalFromSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int preferredRefreshInterval = Integer.valueOf(preferences.getString(getContext().getResources().getString(R.string.pref_key_refresh_interval), "-1"));
        if (preferredRefreshInterval > 5) {
            refreshInterval = preferredRefreshInterval * 1000;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        ListView listView = (ListView) view.findViewById(R.id.tweet_list);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSearchResults();
            }
        });

        listView.setAdapter(adapter);

        return view;
    }

    /**
     * Refresh search results using the SwipeRefreshLayout progress indicator
     */
    private void refreshSearchResults() {
        if (adapter == null || !SearchApplication.isConnectedToInternet()) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast or some other action
            }
        });
    }

    public void search(String query) {
        if(query.equals("")) return;

        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(query)
                .maxItemsPerRequest(10)
                .build();

        adapter = new TweetTimelineListAdapter.Builder(getContext())
                .setTimeline(searchTimeline)
                .build();

    }

    @Override
    public void onResume() {
        //set search to refresh automatically every "refreshInterval" seconds
        searchRefreshHandler.postDelayed(new Runnable() {
            public void run() {
                //do something

                refreshSearchResults();
                runnable = this;
                searchRefreshHandler.postDelayed(runnable, refreshInterval);
            }
        }, refreshInterval);

        super.onResume();
    }

    @Override
    public void onPause() {
        searchRefreshHandler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
}
