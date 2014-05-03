package com.example.confluence.dbtypes;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsFeedQuestion {
	
	public NewsFeedQuestion(String id, String lang, String q, String audio, String user){
		mId = id;
		mLanguage = lang;
		mQuestionPhrase = q;
		mAudio = audio;
		mUser = user;
	}
	
	public NewsFeedQuestion(JSONObject q) throws JSONException{
		this(q.getString("_id"), q.getString("lang"), q.getString("question"), "na", q.getString("user"));
	}
	
	/**
	 * Access method for Question id
	 */
	public String getId(){
		return mId;
	}
	
	/**
	 * Access method for question's language
	 */
	public String getLanguage(){
		return mLanguage;
	}
	
	
	/**
	 * Access method for the question Text
	 */
	public String getQuestion(){
		return mQuestionPhrase;
	}
	
	/**
	 * @return - The path to question's audio, returns null if no audio
	 */
	public String getAudioPath(){
		return mAudio;
	}
	
	/**
	 * @return access function for mVerified variable
	 */
	public boolean isVerified(){
		return mVerified;
	}
	
	/**
	 * @return - access function for mNumberAnswers variable
	 */
	public int numResponses(){
		return mNumAnswers;
	}
	
	/**
	 * Returns this question's user's ID
	 * @return - the ID
	 */
	public String getUser(){
		return mUser;
	}
	
	public String toString(){
		String val = getQuestion() + "\n" + getLanguage()+ "\t\t\t\t\t\t\t\t\t\t\t\t\t";
		
		/*= String.format("%s\nLanguage: %s\nAnswered: %s \t\t\tResponses: %d", 
				getQuestion(), getLanguage(), (isVerified())?"Yes":"No", numResponses());*/
		
		if (isVerified()) {
			val += numResponses() + " answers";
		} else {
			val += "0 answers";
		}
		
		return val;
	}
	
	/* Unique id for this question */
	private String mId;
	
	/* Language this question is asked in/about */
	private String mLanguage;
	
	/* Word/Phrase of this question */
	private String mQuestionPhrase;
	
	/* Path to audio file for this question, null if no audio with question */
	private String mAudio;
	
	
	/* Integer of the number of answers posted */
	private int mNumAnswers;
	
	/* Boolean saying whether there has been a verified/accepted answer yet */
	private boolean mVerified;
	
	/* User's id */
	private String mUser;
}
