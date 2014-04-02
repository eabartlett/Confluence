package com.example.confluence.newsfeed;

import java.util.List;

public class NewsFeedQuestion {
	
	public NewsFeedQuestion(long id, String lang, String q, String audio, List<String> qType){
		this.id = id;
		language = lang;
		questionPhrase = q;
		this.audio = audio;
		questionType = qType;
	}
	
	/**
	 * Access method for Question id
	 */
	public long getId(){
		return id;
	}
	
	/**
	 * Access method for question's language
	 */
	public String getLanguage(){
		return language;
	}
	
	/**
	 * Access method for the question Text
	 */
	public String getQuestion(){
		return questionPhrase;
	}
	
	/**
	 * @return - The path to question's audio, returns null if no audio
	 */
	public String getAudioPath(){
		return audio;
	}
	
	/**
	 * Access method for the question type of this question
	 */
	public List<String> getQuestionType(){
		return questionType;
	}
	
	/* Unique id for this question */
	private long id;
	
	/* Language of this question */
	private String language;
	
	/* Word/Phrase of this question */
	private String questionPhrase;
	
	/* Path to audio file for this question, null if no audio with question */
	private String audio;
	
	/* List of what type of question this question is
	 * i.e pronunciation, grammar, etc.
	 */
	private List<String> questionType;
}
