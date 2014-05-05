package com.example.confluence.answers;

import java.util.ArrayList;

import com.example.confluence.dbtypes.Answer;

/** 
 * This class is deprecated. Only use if you need to get 
 * pre-set answers.
 * @author brian
 *
 */
public class AnswerList {
	
	public static ArrayList<Answer> getDummyAnswers() {
		ArrayList<Answer> dummyAnswers = new ArrayList<Answer>();
		Answer a0 = new Answer("01", "Greg", 
				"That's a good attempt, but you're prounouncing the 'r' wrong. I'll upload a pronunciation later",
				null);
		Answer a1 = new Answer("02", "Christine",
				"You'll want to be careful about your tonation as well.",
				null);
		dummyAnswers.add(a0);
		dummyAnswers.add(a1);
		return dummyAnswers;
	}
}
