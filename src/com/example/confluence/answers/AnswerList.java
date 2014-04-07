package com.example.confluence.answers;

import java.util.ArrayList;


public class AnswerList {

	private ArrayList<Answer> mAnswers;
	
	public AnswerList(){
		mAnswers = new ArrayList<Answer>();
		Answer a0 = new Answer("Greg", 
				"That's a good attempt, but you're prounouncing the 'r' wrong. Here's a correct pronunciation",
				null);
		Answer a1 = new Answer("Christine",
				"You'll want to be careful about your tonation as well.",
				null);
		mAnswers.add(a0);
		mAnswers.add(a1);
	}
	
	public ArrayList<Answer> getAnswers() {
		return mAnswers;
	}
	
	public void addAnswer(Answer answer) {
		mAnswers.add(answer);
	}
	
	public void addAnswer(String user, String answer) {
		Answer newAnswer = new Answer(user, answer, null);
		mAnswers.add(newAnswer);
	}
}
