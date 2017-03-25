package com.actest.nick.hashtagsearch2.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by NICK on 3/25/2017.
 */
public class SearchHistoryLoader extends AsyncTaskLoader<ArrayList<String>> {
    private String filename;
    private static final String TAG = "SearchHistoryLoader";
    public SearchHistoryLoader(Context context, String filename) {
        super(context);
        this.filename = filename;
    }

    @Override
    public ArrayList<String> loadInBackground() {
        Log.d(TAG, "loadInBackground: STARTING");
        FileInputStream fis;

        try {

            fis = getContext().openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<String> returnlist = (ArrayList<String>) ois.readObject();

            //todo: testing
            if (returnlist == null || returnlist.size() == 0) {
                Log.d(TAG, "loadInBackground: default list");

                returnlist.add("#androido");
                returnlist.add("#perfMatters");
            }
            ois.close();
            return returnlist;
        } catch (FileNotFoundException e) {
            //todo: be better with errors
            //return new ArrayList<String>();
        } catch (Exception e) {
            //Log.e(TAG, "loadInBackground: ", e);

        }
        ArrayList<String> errorList = new ArrayList<>();
        errorList.add("#androido");
        errorList.add("#perfMatters");
        return errorList;
    }
}

