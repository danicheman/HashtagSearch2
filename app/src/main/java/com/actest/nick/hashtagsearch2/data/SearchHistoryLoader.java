package com.actest.nick.hashtagsearch2.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Load from search history file
 */
public class SearchHistoryLoader extends AsyncTaskLoader<ArrayList<String>> {
    private final String filename;
    private static final String TAG = "SearchHistoryLoader";

    public SearchHistoryLoader(Context context, String filename) {
        super(context);
        this.filename = filename;
    }

    @Override
    public ArrayList<String> loadInBackground() {
        FileInputStream fis;

        try {

            fis = getContext().openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<String> returnlist = (ArrayList<String>) ois.readObject();

            if (returnlist == null || returnlist.size() == 0) {
                throw new FileNotFoundException();
            }
            ois.close();
            return returnlist;
        } catch (FileNotFoundException e) {
            Log.i(TAG, "loadInBackground: No previous searches found, loading default list");
            ArrayList<String> defaultSearchList = new ArrayList<>();
            defaultSearchList.add("#androido");
            defaultSearchList.add("#perfMatters");
            return defaultSearchList;

        } catch (Exception e) {
            Log.e(TAG, "loadInBackground: ", e);
            return null;
        }

    }
}

