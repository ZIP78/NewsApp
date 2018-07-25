package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Paul on 11/3/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsClass> {

    public static Context mContext;


    public NewsAdapter(Context context, List<NewsClass> news) {

        super(context, 0, news);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }
        NewsClass currentNews = getItem(position);

        //title of the news article
        TextView newsTitle = (TextView) listItemView.findViewById(R.id.title_article);
        newsTitle.setText(currentNews.getmTitle());

        //type of news
        TextView newsType = (TextView) listItemView.findViewById(R.id.typeNews);
        newsType.setText(currentNews.getMtype());

        //author
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentNews.getmTag());

        //date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(formatDate(currentNews.getmTime()));

        return listItemView;
    }

    private String formatDate(String dateString) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");

        Date dateObject = null;
        try {
            dateObject = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat("MM dd, yyyy hh:mm a");
        String date = simpleDateFormat.format(dateObject);

        return date;
    }
}