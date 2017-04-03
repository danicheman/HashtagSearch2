package com.actest.nick.hashtagsearch2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of previous search queries
 */
public class SearchHistoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private SearchHistoryAccessibleInterface searchHistoryAccessor;

    private ListView mSearchHistoryListView;

    private static final String TAG = "SearchHistoryFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);

        mSearchHistoryListView = (ListView) view.findViewById(R.id.list_search_history);
        mSearchHistoryListView.setOnItemClickListener(this);
        //start loading search history

        String jsonSearchHistory = getContext().getSharedPreferences(SearchActivity.SEARCH_HISTORY_PREFS, MODE_PRIVATE)
                .getString(SearchActivity.SEARCH_HISTORY_KEY, "");

        @SuppressWarnings("unchecked")
        ArrayList<String> searchHistory = new Gson()
                    .fromJson(jsonSearchHistory, ArrayList.class);

        if (searchHistory == null) {
            searchHistory = populateSearchHistory();
        }

        searchHistoryAccessor.setSearchHistory(searchHistory);
        mSearchHistoryListView.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                searchHistory
        ));

        return view;
    }

    @NonNull
    private ArrayList<String> populateSearchHistory() {
        ArrayList<String> searchHistory;
        searchHistory = new ArrayList<>();
        searchHistory.add("#perfMatters");
        searchHistory.add("#androidO");
        return searchHistory;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String query = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }

    interface SearchHistoryAccessibleInterface {

        void setSearchHistory(ArrayList<String> searchHistory);

        String getSearchHistoryFilename();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SearchHistoryAccessibleInterface) {
            searchHistoryAccessor = (SearchHistoryAccessibleInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchHistoryAccessibleInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchHistoryAccessor = null;
    }
}
