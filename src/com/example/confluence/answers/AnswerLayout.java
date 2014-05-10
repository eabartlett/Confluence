package com.example.confluence.answers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.confluence.AnswerActivity;
import com.example.confluence.R;
import com.example.confluence.dbtypes.Answer;

public class AnswerLayout extends LinearLayout {
	
	private Answer mAnswer;
	private TextView mTitle, mAnswerText, mRatingText;
	private ImageButton mPlaybackButton, mUpvoteButton, mDownvoteButton;
	private boolean mUpClicked = false, mDownClicked = false;
	private Activity mActivity;
	private String mAnswerId;
	private String mAudioPath;

	public AnswerLayout(Context context) {
		super(context);
	}
	
	public AnswerLayout(Context context, Answer answer) {
		super(context, null);
		
		mActivity = (Activity) context;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.answer_view, this, true);
		
		// TODO: check if ids will conflict
		this.mAnswer = answer;
		mAnswerId = this.mAnswer.getAnswerId();
		mAudioPath = mAnswer.getAudioPath();
		mTitle = (TextView) findViewById(R.id.answer_firstLine);
		Log.d("CONFLUENCE", mTitle + " " + mAnswerId + " " + mAudioPath);

		mAnswerText = (TextView) findViewById(R.id.answer_secondLine);
		mRatingText = (TextView) findViewById(R.id.answer_rating);
		mUpvoteButton = (ImageButton) findViewById(R.id.answer_upvote);
		mDownvoteButton = (ImageButton) findViewById(R.id.answer_downvote);
		
		// Display playback button if recording exists
		
		mPlaybackButton = (ImageButton) findViewById(R.id.answer_attachment);
		if (mAnswer.hasRecording()){
			mPlaybackButton.setVisibility(VISIBLE);
			mPlaybackButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playAudio(mAnswerId, mAudioPath);
				}
			});
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
		setRating(answer.getRating());
	}
	
	protected void playAudio(String answerId, String audioPath) {
		((AnswerActivity) mActivity).playAudioInAnswer(answerId, audioPath);
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
	
	/**
	 * Inserts rating value into view.
	 * @param rating
	 */
	protected void setRating(int rating) {
		mRatingText.setText(Integer.toString(rating));
	}
	
	/**
	 * Handles logic for upvoting.
	 */
	protected void incrementRating() {
		if (mDownClicked) {
			// do nothing because already downvoted
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

