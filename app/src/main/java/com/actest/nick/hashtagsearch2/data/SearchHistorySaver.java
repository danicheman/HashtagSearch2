package com.actest.nick.hashtagsearch2.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Save to search history file
 */
public class SearchHistorySaver extends AsyncTaskLoader<Boolean> {

    private static final String TAG = "SearchHistorySaver";
    private final ArrayList<String> mSearchHistory;
    private final String mFilename;

    public SearchHistorySaver(Context context, ArrayList<String> searchHistory, String filename) {
        super(context);
        this.mSearchHistory = searchHistory;
        this.mFilename = filename;
    }

    @Override
    public Boolean loadInBackground() {
        //if there was nothing to do, return true
        if (mSearchHistory.size() == 0) return true;
        try {
            FileOutputStream fos = getContext().openFileOutput(mFilename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mSearchHistory);
            oos.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "loadInBackground: ", e);
        }
        return false;
    }
}
