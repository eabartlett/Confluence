package com.example.confluence.newsfeed;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.confluence.R;
import com.example.confluence.dbtypes.NewsFeedQuestion;

public class NewsArrayAdapter extends ArrayAdapter<NewsFeedQuestion> {

//	private final Context context;
	
	public NewsArrayAdapter(Context context, int resource, NewsFeedQuestion[] questions) {
		super(context, resource, questions);
//		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null){
	        convertView = super.getView(position, convertView, parent);
		}
		NewsFeedQuestion question = (NewsFeedQuestion) getItem(position);
		//return new NewsFeedQuestionView(getContext(), question);
		NewsFeedQuestionView questionView = new NewsFeedQuestionView(getContext(), question);
	
		/*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.news_list_view,  parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		textView.setText();*/
		
		final View view = View.inflate(getContext(), R.layout.list_item, null);
		((ViewGroup) view).addView(questionView);
		return view;
		
		

		
	}

}
