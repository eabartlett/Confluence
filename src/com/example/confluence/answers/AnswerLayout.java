package com.example.confluence.answers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.confluence.R;

public class AnswerLayout extends LinearLayout {
	
	private Answer mAnswer;
	private TextView mTitle, mAnswerText, mRatingText;
	private ImageButton mPlaybackButton, mUpvoteButton, mDownvoteButton;
	private boolean mUpClicked = false, mDownClicked = false;

	public AnswerLayout(Context context, Answer answer) {
		super(context, null);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.answer_view, this, true);
		
		// TODO: check if ids will conflict
		this.mAnswer = answer;
		mTitle = (TextView) findViewById(R.id.answer_firstLine);
		mAnswerText = (TextView) findViewById(R.id.answer_secondLine);
		mRatingText = (TextView) findViewById(R.id.answer_rating);
		mUpvoteButton = (ImageButton) findViewById(R.id.answer_upvote);
		mDownvoteButton = (ImageButton) findViewById(R.id.answer_downvote);
		
		// Display playback button if recording exists
		mPlaybackButton = (ImageButton) findViewById(R.id.answer_attatchment);
		if (answer.hasRecording()) {
			mPlaybackButton.setVisibility(VISIBLE);
		}
		
		// Add listeners to upvotes
		mUpvoteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				incrementRating();
			}
		});
		
		mDownvoteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				decrementRating();
			}
		});
		
		// Insert data into UI
		setUserName(answer.getUserName());
		setAnswer(answer.getText());
	}
	
	/**
	 * Sets title of answer to user's name. 
	 * @param text
	 */
	protected void setUserName(String name) {
		mTitle.setText(name + " answered:");
	}
	
	/**
	 * Inserts answer into view. 
	 * @param answer String of user's answer.
	 */
	protected void setAnswer(String answer) {
		mAnswerText.setText(answer);
	}
	
	protected void incrementRating() {
		if (mDownClicked) {
			// do nothing because already voted
		} else {
			if (mUpClicked) {
				// undo upvote
				mUpClicked = false;
				mAnswer.decrementRating();
				mUpvoteButton.setBackgroundColor(getResources().getColor(R.color.light_gray));
			} else {
				mUpClicked = true;
				mAnswer.incrementRating();
				mUpvoteButton.setBackgroundColor(getResources().getColor(R.color.green));
			}
			mRatingText.setText(Integer.toString(mAnswer.getRating()));
		}
	}
	
	protected void decrementRating() {
		if (mUpClicked) {
			// do nothing because already upvoted
		} else {
			if (mDownClicked) {
				// undo downvote
				mDownClicked = false;
				mAnswer.incrementRating();
				mDownvoteButton.setBackgroundColor(getResources().getColor(R.color.light_gray));
			} else {
				mDownClicked = true;
				mAnswer.decrementRating();
				mDownvoteButton.setBackgroundColor(getResources().getColor(R.color.red));
			}
		}
		mRatingText.setText(Integer.toString(mAnswer.getRating()));
	}
}

