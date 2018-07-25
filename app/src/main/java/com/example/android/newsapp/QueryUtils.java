package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp.NewsMainActivity.LOG_TAG;

/**
 * Created by Paul on 11/3/2017.
 */

public final class QueryUtils {


    private QueryUtils() {

    }

    public static List<NewsClass> extractFeatureFromJSON(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        //create empty ArrayList
        List<NewsClass> news = new ArrayList<>();
        //try block to parse the JSON
        try {
            // the parsing process
            JSONObject jsonResponse = new JSONObject(newsJSON);

            if (jsonResponse.has("response")) {
                JSONObject responseObject = jsonResponse.getJSONObject("response");


                if (responseObject.has("results")) {
                    JSONArray jsonArray = responseObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject currentNews = jsonArray.getJSONObject(i);

                        String title = "N/A";
                        if (currentNews.has("webTitle")) {
                            title = currentNews.getString("webTitle");
                        }

                        String url = "N/A";
                        if (currentNews.has("webUrl")) {
                            url = currentNews.getString("webUrl");
                        }

                        String time = "N/A";
                        if (currentNews.has("webPublicationDate")) {
                            time = currentNews.getString("webPublicationDate"); //on standby
                        }

                        String typeNews = "N/A";
                        if (currentNews.has("sectionName")) {
                            typeNews = currentNews.getString("sectionName");
                        }

                        String tags = "N/A";
                        if (currentNews.has("tags")) {

                            JSONArray tagsArray = currentNews.getJSONArray("tags");
                            for (int t = 0; t < tagsArray.length(); t++) {

                                JSONObject cuurentTag = tagsArray.getJSONObject(t);

                                if (cuurentTag.has("webTitle")) {
                                    tags = cuurentTag.getString("webTitle");
                                }
                            }
                        }

                        NewsClass newss = new NewsClass(title, url, time, typeNews, tags);
                        news.add(newss);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return news;
    }

    //the networking process
    public static List fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List news = extractFeatureFromJSON(jsonResponse);

        // Return the {@link Event}
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
