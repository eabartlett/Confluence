package com.example.confluence.answers;

import java.util.Date;

/**
 * Class that holds the information for an Answer to a question.
 * Includes user, answer text, date, etc.
 * @author brian
 *
 */
public class Answer {
	
	// TODO: implement User object instead of userId
	// TODO: implement list of responses to answer
	private String mUserId;
	private String mUserName, mAnswerText, mAudioPath;
	private boolean mHasRecording;
	private Date mAnswerDate;
	private int mRating; // rating of an answer
	private String mLanguage; // Language of question/answer

	public Answer(String userName, String answerText, boolean hasRecording, String recordFilePath) { 
		mUserName = userName;
		mAnswerText = answerText;
		mAnswerDate = null;
		mHasRecording = hasRecording;
		mRating = 0;
	}
	
	public String getUserId() {
		return mUserId;
	}
	
	public String getUserName() {
		return mUserName;
	}
	
	public String getText() {
		return mAnswerText;
	}
	
	protected Date getDate() {
		return mAnswerDate;
	}
	
	protected int getRating() {
		return mRating;
	}
	
	protected void incrementRating() {
		mRating++;
	}
	
	protected void decrementRating() {
		mRating--;
	}
	
	public String getAudioPath() {
		return mAudioPath;
	}
	
	public boolean hasAudio() {
		if (mAudioPath != null) {
			return true;
		} return false;
	}
	
	protected boolean hasRecording() {
		return mHasRecording;
	}
	
	public String getLanguage(){
		return mLanguage;
	}
}
