package com.example.confluence.answers;

import java.util.Date;

import android.media.AudioRecord;

/**
 * Class that holds the information for an Answer to a question.
 * Includes user, answer text, date, etc.
 * @author brian
 *
 */
public class Answer {
	
	// TODO: implement User object instead of userId
	// TODO: implement list of responses to answer
	private int mUserId;
	private String mUserName;
	private String mAnswerText;
	private String mAudioPath;
	private Date mAnswerDate;
	private int mRating; // rating of an answer

	public Answer(String userName, String answerText, Date answerDate) { 
		mUserName = userName;
		mAnswerText = answerText;
		mAnswerDate = answerDate; 
		mRating = 0;
	}
	
	protected int getUserId() {
		return mUserId;
	}
	
	protected String getUserName() {
		return mUserName;
	}
	
	protected String getText() {
		return mAnswerText;
	}
	
	protected Date getDate() {
		return mAnswerDate;
	}
	
	protected int getRating() {
		return mRating;
	}
	
	protected String getAudioPath() {
		return mAudioPath;
	}
	
	protected boolean hasAudio() {
		if (mAudioPath != null) {
			return true;
		} return false;
	}
}
