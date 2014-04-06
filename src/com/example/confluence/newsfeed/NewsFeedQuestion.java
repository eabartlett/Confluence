package com.example.confluence.newsfeed;

public class NewsFeedQuestion {
	
	public NewsFeedQuestion(long id, String langTo, String langFrom, String q, String audio, String qType){
		this.id = id;
		languageTo = langTo;
		languageFrom = langFrom;
		questionPhrase = q;
		this.audio = audio;
		questionType = qType;
	}
	
	public void setAudioPath(String path){
		audio = path;
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
	public String getLanguageTo(){
		return languageTo;
	}
	
	/**
	 * Access method for question's language
	 */
	public String getLanguageFrom(){
		return languageFrom;
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
	public String getQuestionType(){
		return questionType;
	}
	
	public String toString(){
		String val = String.format("%s\nType: %s\nLanguage: %s ", 
				getQuestion(), getQuestionType(), getLanguageTo());
		
		return val;
	}
	
	/* Unique id for this question */
	private long id;
	
	/* Language this question is asked about */
	private String languageTo;
	
	/* Language this question is asked in */
	private String languageFrom;
	
	/* Word/Phrase of this question */
	private String questionPhrase;
	
	/* Path to audio file for this question, null if no audio with question */
	private String audio;
	
	/* List of what type of question this question is
	 * i.e pronunciation, grammar, etc.
	 */
	private String questionType;
}
