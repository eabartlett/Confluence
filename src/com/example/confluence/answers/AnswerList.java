package com.example.confluence.answers;

import java.util.ArrayList;


public class AnswerList {

	private ArrayList<Answer> mAnswers;
	
	public AnswerList(){
		mAnswers = new ArrayList<Answer>();
	}
	
	public ArrayList<Answer> getAnswers() {
		return mAnswers;
	}
	
	public void addAnswer(Answer answer) {
		mAnswers.add(answer);
	}
	
	public void addAnswer(String user, String answer, boolean hasRecording) {
		Answer newAnswer = new Answer(user, answer, hasRecording);
		mAnswers.add(newAnswer);
	}
	
	public static ArrayList<Answer> getDummyAnswers() {
		ArrayList<Answer> dummyAnswers = new ArrayList<Answer>();
		Answer a0 = new Answer("Greg", 
				"That's a good attempt, but you're prounouncing the 'r' wrong. Here's a correct pronunciation",
				true);
		Answer a1 = new Answer("Christine",
				"You'll want to be careful about your tonation as well.",
				false);
		dummyAnswers.add(a0);
		dummyAnswers.add(a1);
		return dummyAnswers;
	}
}
