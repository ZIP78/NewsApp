package com.example.android.newsapp;

/**
 * Created by Paul on 11/3/2017.
 */

public class NewsClass {

    private String mTitle;
    private String mTime;
    private String murl;
    private String mtype;
    private String mTag;

    public NewsClass(String title, String url, String time, String typeNews, String tag) {

        mTitle = title;
        mTime = time;
        murl = url;
        mtype = typeNews;
        mTag = tag;
    }

    public String getmTitle() {

        return mTitle;
    }

    public String getmTime() {
        return mTime;
    }

    public String getMurl() {
        return murl;
    }

    public String getMtype() {
        return mtype;
    }

    public String getmTag() {
        return mTag;
    }
}