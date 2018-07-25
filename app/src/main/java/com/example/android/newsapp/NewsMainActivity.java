package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsMainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsClass>> {

    static final String LOG_TAG = NewsMainActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private NewsAdapter mAdapter;
    private TextView mEmptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);

        mAdapter = new NewsAdapter(this, new ArrayList<NewsClass>());

        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateView = (TextView) findViewById(R.id.empty_page);
        newsListView.setEmptyView(mEmptyStateView);


        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsClass currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getMurl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(websiteIntent);

                }
            }
        });

        //check the state of the connectivity
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // get details of the network
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            if (loaderManager.getLoader(BOOK_LOADER_ID) != null) {
                loaderManager.restartLoader(BOOK_LOADER_ID, null, NewsMainActivity.this);
            } else
                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(BOOK_LOADER_ID, null, NewsMainActivity.this);


        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            //display error
            mEmptyStateView.setText(R.string.no_net);
        }

    }

    @Override
    public Loader<List<NewsClass>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
        String searchCategory = sharedprefs.getString(

                getString(R.string.settings_search_category_key),
                getString(R.string.settings_search_category_default));

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("q", searchCategory)
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("api-key", "test");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsClass>> loader, List<NewsClass> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        mEmptyStateView.setText(R.string.empty_page);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsClass>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, settingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}