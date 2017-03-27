package com.actest.nick.hashtagsearch2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actest.nick.hashtagsearch2.data.SearchHistoryLoader;

import java.util.ArrayList;

/**
 * A fragment representing a list of previous search queries
 */
public class SearchHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<String>>, AdapterView.OnItemClickListener {

    private static final String TAG = "SearchHistoryFragment";

    private SearchHistoryAccessibleInterface searchHistoryAccessor;

    private ListView mSearchHistoryListView;

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
        getLoaderManager().initLoader(0, null, this).forceLoad();

        return view;
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starting searchhistoryloader");
        return new SearchHistoryLoader(getContext(), searchHistoryAccessor.getSearchHistoryFilename());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
        searchHistoryAccessor.setSearchHistory(data);
        Log.d(TAG, "onLoadFinished: " + data.toString());
        mSearchHistoryListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, data));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {
        Log.d(TAG, "onLoaderReset: RESET?");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String query = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
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
