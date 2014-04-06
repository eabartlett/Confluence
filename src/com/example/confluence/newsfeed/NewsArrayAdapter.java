package com.example.confluence.newsfeed;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsArrayAdapter<T> extends ArrayAdapter<NewsFeedQuestion> {

	private final Context context;
	
	public NewsArrayAdapter(Context context, int resource, NewsFeedQuestion[] questions) {
		super(context, resource, questions);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		NewsFeedQuestion question = (NewsFeedQuestion) getItem(position);
		return new NewsFeedQuestionView(getContext(), question);
	
		/*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout,  parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		textView.setText();*/
		
	}

}
