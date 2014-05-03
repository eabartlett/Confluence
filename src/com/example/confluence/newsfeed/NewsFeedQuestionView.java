package com.example.confluence.newsfeed;

import android.content.Context;
import android.widget.TextView;
import com.example.confluence.dbtypes.*;

/**
 * Custom View class for each item in the NewsFeedActivity
 * Currently extends TextView only as far as adding in a variable
 * mQuestion, to be used in filtering, and to change how it gets it's
 * text value.
 */
public class NewsFeedQuestionView extends TextView {

	public NewsFeedQuestionView(Context context, NewsFeedQuestion question) {
		super(context);
		mQuestion = question;
		if(mQuestion != null)
			setText(mQuestion.toString());
	}
	
	public NewsFeedQuestion getQuestion(){
		return mQuestion;
	}

	//The Question object of this view
	private NewsFeedQuestion mQuestion;
	
}
