package com.example.confluence.newsfeed;

import com.example.confluence.NewsFeedQuestionView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

<<<<<<< HEAD
public class NewsArrayAdapter<T> extends ArrayAdapter<NewsFeedQuestion> {

	public NewsArrayAdapter(Context context, int resource, NewsFeedQuestion[] questions) {
		super(context, resource, questions);
=======
public class NewsArrayAdapter<T> extends ArrayAdapter<T> {

	public NewsArrayAdapter(Context context, int resource) {
		super(context, resource);
>>>>>>> 48506854d08cdd8861e60e396c36cc5c267236b8
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		NewsFeedQuestion question = (NewsFeedQuestion) getItem(position);
		return new NewsFeedQuestionView(getContext(), question);
	}

}
