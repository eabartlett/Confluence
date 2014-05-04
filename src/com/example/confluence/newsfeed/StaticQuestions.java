package com.example.confluence.newsfeed;

import java.util.LinkedList;
import java.util.HashMap;

import com.example.confluence.dbtypes.NewsFeedQuestion;

import android.util.Log;

public class StaticQuestions {
	private NewsFeedQuestion[] mQuestions;
	private HashMap<String, NewsFeedQuestion[]> cachedFilters = new HashMap<String, NewsFeedQuestion[]>();
	
	public StaticQuestions(){
		mQuestions = new NewsFeedQuestion[20];
//		NewsFeedQuestion q1 = new NewsFeedQuestion(0, "English", "Chameleon", null, 0, false);
//		NewsFeedQuestion q2 = new NewsFeedQuestion(0, "French", "Tu t'appelles comment?", null, 0, false);
//		NewsFeedQuestion q3 = new NewsFeedQuestion(0, "English", "Generically", "tba", 0, false );
//		NewsFeedQuestion q4 = new NewsFeedQuestion(0, "Spanish", "No sé lo que estoy haciendo", "tba", 1, true );
//		NewsFeedQuestion q5 = new NewsFeedQuestion(0, "French", "Chat", "tba", 2, true);
//		NewsFeedQuestion q6 = new NewsFeedQuestion(0, "French", "J'ai oublie beaucoup des langues", null, 3, false);
//		NewsFeedQuestion q7 = new NewsFeedQuestion(0, "German", "Schau mich an, ich bin kühl", "tba", 1, false);
//		NewsFeedQuestion q8 = new NewsFeedQuestion(0, "English", "Chaos", "tba", 2, true);
//		NewsFeedQuestion q9 = new NewsFeedQuestion(0, "Spanish", "Yogur", "tba", 0, false);
//		NewsFeedQuestion q10 = new NewsFeedQuestion(0, "French", "Quelqu'un m'a dit", "tba", 1, false);
//		NewsFeedQuestion q11 = new NewsFeedQuestion(0, "English", "Is this their stuff?", null, 1, false);
//		NewsFeedQuestion q12 = new NewsFeedQuestion(0, "French", "Elle est mignonne.", null, 1, false);
//		NewsFeedQuestion q13 = new NewsFeedQuestion(0, "English", "Read vs. Read", null, 1, false);
//		NewsFeedQuestion q14 = new NewsFeedQuestion(0, "French", "J'adore les chats", null, 1, false);
//		NewsFeedQuestion q15 = new NewsFeedQuestion(0, "German", "Sprechen", "tba", 2, true);
//		NewsFeedQuestion q16 = new NewsFeedQuestion(0, "Spanish", "Decir", "tba", 2, true);
//		NewsFeedQuestion q17 = new NewsFeedQuestion(0, "German", "Sie haben Menschen die Hände", null, 2, true);
//		NewsFeedQuestion q18 = new NewsFeedQuestion(0, "English", "Answer, Daily Double", "tba", 2, true);
//		NewsFeedQuestion q19 = new NewsFeedQuestion(0, "German", "Antwort, Tages doppelt", "tba", 2, true);
//		NewsFeedQuestion q20 = new NewsFeedQuestion(0, "English", "I red that somewhere.", "tba", 2, true);
//		mQuestions[0] = q1;
//		mQuestions[1] = q2;
//		mQuestions[2] = q3;
//		mQuestions[3] = q4;
//		mQuestions[4] = q5;
//		mQuestions[5] = q6;
//		mQuestions[6] = q7;
//		mQuestions[7] = q8;
//		mQuestions[8] = q9;
//		mQuestions[9] = q10;
//		mQuestions[10] = q11;
//		mQuestions[11] = q12;
//		mQuestions[12] = q13;
//		mQuestions[13] = q14;
//		mQuestions[14] = q15;
//		mQuestions[15] = q16;
//		mQuestions[16] = q17;
//		mQuestions[17] = q18;
//		mQuestions[18] = q19;
//		mQuestions[19] = q20;
	}
	
	public NewsFeedQuestion[] getQuestions(String filter){

		LinkedList<NewsFeedQuestion> questions;
		Log.i("Filter", filter);
		if (cachedFilters.get(filter) == null) {
			questions = new LinkedList<NewsFeedQuestion>();
			for (NewsFeedQuestion q : mQuestions) {
				if (q.getLanguage().equals(filter)) {
					Log.i("Question language", q.getLanguage());
					questions.add(q);
				}
			}
			return questions.toArray(new NewsFeedQuestion[questions.size()]);
		}
		return cachedFilters.get(filter);
	}
	
	
	public NewsFeedQuestion[] getQuestions(){
		return mQuestions;
	}
}
