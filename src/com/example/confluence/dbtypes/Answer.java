package com.example.confluence.dbtypes;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

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
	private String mQId;
	private String mUserName, mAnswerText, mAudioPath;
	private boolean mHasRecording;
	private Date mAnswerDate;
	private int mRating; // rating of an answer
	private String mLanguage; // Language of question/answer

	public Answer(String id, String userId, String answerText,String recordFilePath, String qId) { 
		mUserId = userId;
		mAnswerText = answerText;
		mAnswerDate = null;
		mAudioPath = recordFilePath;
		mRating = 0;
		mQId = qId;
	}
	
	public Answer(JSONObject q) throws JSONException {
		// TODO Auto-generated constructor stub
		this(q.getString("_id"), q.getString("user"), q.getString("answer"), q.getString("audio"), /*QId*/q.getString(""));
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
	
	public Date getDate() {
		return mAnswerDate;
	}
	
	public int getRating() {
		return mRating;
	}
	
	public void incrementRating() {
		mRating++;
	}
	
	public void decrementRating() {
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
	
	public boolean hasRecording() {
		return mHasRecording;
	}
	
	public String getLanguage(){
		return mLanguage;
	}
	
	public String getQId(){
		return mQId;
	}
}
