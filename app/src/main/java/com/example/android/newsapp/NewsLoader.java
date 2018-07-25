package com.example.android.newsapp;

import android.content.Context;

import java.util.List;

/**
 * Created by Paul on 11/5/2017.
 */

public class NewsLoader extends android.content.AsyncTaskLoader<List<NewsClass>> {

    private String mURL;

    public NewsLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsClass> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<NewsClass> results = QueryUtils.fetchNewsData(mURL);
        return results;

    }
}