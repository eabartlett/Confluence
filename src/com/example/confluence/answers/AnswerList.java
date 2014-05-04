package com.example.confluence.answers;

import java.util.ArrayList;

/** 
 * This class is deprecated. Only use if you need to get 
 * pre-set answers.
 * @author brian
 *
 */
public class AnswerList {
	
	public static ArrayList<Answer> getDummyAnswers() {
		ArrayList<Answer> dummyAnswers = new ArrayList<Answer>();
		Answer a0 = new Answer("Greg", 
				"That's a good attempt, but you're prounouncing the 'r' wrong. I'll upload a pronunciation later",
				false,
				null);
		Answer a1 = new Answer("Christine",
				"You'll want to be careful about your tonation as well.",
				false,
				null);
		dummyAnswers.add(a0);
		dummyAnswers.add(a1);
		return dummyAnswers;
	}
}
