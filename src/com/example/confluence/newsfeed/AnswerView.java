package com.example.confluence.newsfeed;

import android.content.Context;
import android.widget.TextView;
import com.example.confluence.dbtypes.*;

public class AnswerView extends TextView {

	public AnswerView(Context context, Answer answer) {
		super(context);
		mAnswer = answer;
		if(mAnswer != null)
			setText(mAnswer.toString());
	}
	
	public Answer getAnswer(){
		return mAnswer;
	}

	//The Answer object of this view
	private Answer mAnswer;
	
}
