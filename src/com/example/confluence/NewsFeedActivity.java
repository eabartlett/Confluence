package com.example.confluence;

import com.example.confluence.newsfeed.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NewsFeedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
		loadFeed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feed, menu);
		return true;
	}

	
	/**
	 * Gets questions from getQuestions method and loads them into the ListView
	 * in the main view. Allows getQuestions to worry about how to get the 
	 * questions.
	 */
	private void loadFeed(){
		NewsArrayAdapter<NewsFeedQuestion> questionArray = getQuestions();
		
	}

	/**
	 * 
	 * @return - Questions for feed (hard-coded for interactive prototype)
	 */
	private NewsArrayAdapter<NewsFeedQuestion> getQuestions() {
		return new NewsArrayAdapter<NewsFeedQuestion>(this, R.array.news_feed_questions);
	}
}
