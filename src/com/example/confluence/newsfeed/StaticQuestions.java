package com.example.confluence.newsfeed;

import java.util.LinkedList;
import java.util.HashMap;

public class StaticQuestions {
	private NewsFeedQuestion[] mQuestions;
	private HashMap<String, NewsFeedQuestion[]> cachedFilters = new HashMap<String, NewsFeedQuestion[]>();
	
	public StaticQuestions(){
		mQuestions = new NewsFeedQuestion[20];
		LinkedList<String> pronunciation = new LinkedList<String>();
		pronunciation.add("Pronunciation");
		LinkedList<String> grammar = new LinkedList<String>();
		pronunciation.add("Grammar");
		LinkedList<String> translation = new LinkedList<String>();
		pronunciation.add("Translation");
		NewsFeedQuestion q1 = new NewsFeedQuestion(0, "English", "English", "Chameleon", null, pronunciation);
		NewsFeedQuestion q2 = new NewsFeedQuestion(0, "French", "French", "Tu t'appelles comment?", null, grammar);
		NewsFeedQuestion q3 = new NewsFeedQuestion(0, "English", "English", "Generically", "tba", pronunciation);
		NewsFeedQuestion q4 = new NewsFeedQuestion(0, "Spanish", "English", "No sé lo que estoy haciendo", "tba", translation);
		NewsFeedQuestion q5 = new NewsFeedQuestion(0, "French", "French", "Chat", "tba", pronunciation);
		NewsFeedQuestion q6 = new NewsFeedQuestion(0, "French", "English", "J'ai oublie beaucoup des langues", null, pronunciation);
		NewsFeedQuestion q7 = new NewsFeedQuestion(0, "German", "English", "Schau mich an, ich bin kühl", "tba", translation);
		NewsFeedQuestion q8 = new NewsFeedQuestion(0, "English", "English", "Chaos", "tba", pronunciation);
		NewsFeedQuestion q9 = new NewsFeedQuestion(0, "Spanish", "Spanish", "Yogur", "tba", pronunciation);
		NewsFeedQuestion q10 = new NewsFeedQuestion(0, "French", "French", "Quelqu'un m'a dit", "tba", pronunciation);
		NewsFeedQuestion q11 = new NewsFeedQuestion(0, "English", "English", "Is this they're stuff?", null, grammar);
		NewsFeedQuestion q12 = new NewsFeedQuestion(0, "French", "French", "Elle est mignon.", null, grammar);
		NewsFeedQuestion q13 = new NewsFeedQuestion(0, "English", "English", "Read vs. Read", null, grammar);
		NewsFeedQuestion q14 = new NewsFeedQuestion(0, "French", "English", "J'adore les chats", null, translation);
		NewsFeedQuestion q15 = new NewsFeedQuestion(0, "German", "German", "Sprechen", "tba", pronunciation);
		NewsFeedQuestion q16 = new NewsFeedQuestion(0, "Spanish", "English", "Decir", "tba", pronunciation);
		NewsFeedQuestion q17 = new NewsFeedQuestion(0, "German", "Spanish", "Sie haben Menschen die Hände", null, translation);
		NewsFeedQuestion q18 = new NewsFeedQuestion(0, "English", "German", "Answer, Daily Double", "tba", translation);
		NewsFeedQuestion q19 = new NewsFeedQuestion(0, "German", "English", "Antwort, Tages doppelt", "tba", translation);
		NewsFeedQuestion q20 = new NewsFeedQuestion(0, "English", "English", "I red that somewhere.", "tba", grammar);
		mQuestions[0] = q1;
		mQuestions[1] = q2;
		mQuestions[2] = q3;
		mQuestions[3] = q4;
		mQuestions[4] = q5;
		mQuestions[5] = q6;
		mQuestions[6] = q7;
		mQuestions[7] = q8;
		mQuestions[8] = q9;
		mQuestions[9] = q10;
		mQuestions[10] = q11;
		mQuestions[11] = q12;
		mQuestions[12] = q13;
		mQuestions[13] = q14;
		mQuestions[14] = q15;
		mQuestions[15] = q16;
		mQuestions[16] = q17;
		mQuestions[17] = q18;
		mQuestions[18] = q19;
		mQuestions[19] = q20;
	}
	
<<<<<<< HEAD
	public NewsFeedQuestion[] getQuestions(String filter){
=======
	public NewsFeedQuestion[] filter(String filter){
>>>>>>> 48506854d08cdd8861e60e396c36cc5c267236b8
		LinkedList<NewsFeedQuestion> questions;
		if (cachedFilters.get(filter) == null) {
			questions = new LinkedList<NewsFeedQuestion>();
			for (NewsFeedQuestion q : mQuestions) {
				if (q.getLanguageFrom().equals(filter)
						|| q.getLanguageTo().equals(filter)) {
					questions.add(q);
				}
			}
			return (NewsFeedQuestion[]) questions.toArray();
		}
		return cachedFilters.get(filter);
	}
<<<<<<< HEAD
	
	public NewsFeedQuestion[] getQuestions(){
		return mQuestions;
	}
=======
>>>>>>> 48506854d08cdd8861e60e396c36cc5c267236b8
}
