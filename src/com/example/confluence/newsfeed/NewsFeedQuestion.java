package com.example.confluence.newsfeed;

public class NewsFeedQuestion {
	
	public NewsFeedQuestion(long id, String lang, String q, String audio, int answers, boolean verified){
		mId = id;
		mLanguage = lang;
		mQuestionPhrase = q;
		mAudio = audio;
		mNumAnswers = answers;
		mVerified = verified;
	}
	
	/**
	 * Access method for Question id
	 */
	public long getId(){
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
	
	public String toString(){
		String val = String.format("%s\nLanguage: %s\nAnswered: %s \t\t\tResponses: %d", 
				getQuestion(), getLanguage(), (isVerified())?"Yes":"No", numResponses());
		
		return val;
	}
	
	/* Unique id for this question */
	private long mId;
	
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
}
