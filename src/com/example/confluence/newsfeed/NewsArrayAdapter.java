package com.example.confluence.newsfeed;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class NewsArrayAdapter<T> extends ArrayAdapter<NewsFeedQuestion> {

	public NewsArrayAdapter(Context context, int resource, NewsFeedQuestion[] questions) {
		super(context, resource, questions);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		NewsFeedQuestion question = (NewsFeedQuestion) getItem(position);
		return new NewsFeedQuestionView(getContext(), question);
	}

}
