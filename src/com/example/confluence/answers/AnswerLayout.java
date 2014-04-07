package com.example.confluence.answers;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.confluence.R;

public class AnswerLayout extends LinearLayout {
	
	private Answer answer;
	private TextView title;
	private TextView answerText;

	public AnswerLayout(Context context, Answer answer) {
		super(context, null);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.answer_view, this, true);
		
		// TODO: check if ids will conflict
		this.answer = answer;
		title = (TextView) findViewById(R.id.answer_firstLine);
		answerText = (TextView) findViewById(R.id.answer_secondLine);
		
		ImageButton attatchment = (ImageButton) findViewById(R.id.answer_attatchment);
		if (answer.hasRecording()) {
			attatchment.setVisibility(VISIBLE);
		}
		
		// Insert data into UI
		setUserName(answer.getUserName());
		setAnswer(answer.getText());
	}
	
	/**
	 * Sets title of answer to user's name. 
	 * @param text
	 */
	protected void setUserName(String name) {
		title.setText(name + " answered:");
	}
	
	/**
	 * Inserts answer into view. 
	 * @param answer String of user's answer.
	 */
	protected void setAnswer(String answer) {
		answerText.setText(answer);
	}

}

