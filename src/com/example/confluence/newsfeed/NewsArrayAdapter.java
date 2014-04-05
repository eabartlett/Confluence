package com.example.confluence.newsfeed;

import com.example.confluence.NewsFeedQuestionView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class NewsArrayAdapter<T> extends ArrayAdapter<T> {

	public NewsArrayAdapter(Context context, int resource) {
		super(context, resource);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		NewsFeedQuestion question = (NewsFeedQuestion) getItem(position);
		return new NewsFeedQuestionView(getContext(), question);
	}

}
