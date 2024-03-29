package com.example.confluence.dbtypes;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject; 

import com.example.confluence.ConfluenceAPI;

import android.os.AsyncTask;
import android.util.Log;

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
	private String mAId;
	private String mUserName, mAnswerText, mAudioPath;
	private boolean mHasRecording;
	private Date mAnswerDate;
	private int mRating; // rating of an answer
	private String mLanguage; // Language of question/answer
	private ConfluenceAPI mApi;

	public Answer(String id, String userName, String answerText,String recordFilePath, String qId, int rating) { 
		mUserName = userName;
		mAnswerText = answerText;
		mAnswerDate = null;
		mAudioPath = recordFilePath;
		mRating = rating;
		mQId = qId;
		mAId = id;
		mApi = new ConfluenceAPI();
		if (recordFilePath == null || recordFilePath.equals("null")) {
			mHasRecording = false;
		} else {
			mHasRecording = true;
		}
	}
	
	public Answer(JSONObject q) throws JSONException {
		// TODO Auto-generated constructor stub
		this(q.getString("_id"), q.getString("user"), q.getString("answer"), q.getString("audio"), /*QId*/q.getString("question"), Integer.parseInt(q.getString("rating")));
		Log.d("Confluence JSON", q.toString(1));
	}

	public String getUserId() {
		return mUserId;
	}
	
	public String getAnswerId() {
		return mAId;
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
		IncrementRating req = new IncrementRating();
		req.execute(mAId);
		mRating++;
	}
	
	public void decrementRating() {
		DecrementRating req = new DecrementRating();
		req.execute(mAId);
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
	
	private class IncrementRating extends AsyncTask<String, Integer, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String aid = params[0];
			mApi.increment(aid);
			return null;
		}
		
	}
	
	private class DecrementRating extends AsyncTask<String, Integer, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String aid = params[0];
			mApi.decrement(aid);
			return null;
		}
		
	}
	
	
}
