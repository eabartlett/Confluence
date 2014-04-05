package com.example.confluence;

import com.example.confluence.newsfeed.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

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
<<<<<<< HEAD
		return new NewsArrayAdapter<NewsFeedQuestion>(this, R.layout.activity_news_feed, mQuestions.getQuestions());
=======
		return new NewsArrayAdapter<NewsFeedQuestion>(this, mQuestions.getQuestions());
>>>>>>> 48506854d08cdd8861e60e396c36cc5c267236b8
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//        if (id == R.id.action_ask) {
//            Intent askQuestionIntent = new Intent(NewsFeedActivity.this, AskQuestionActivity.class);
//            NewsFeedActivity.this.startActivity(askQuestionIntent);
//
//            return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	//TODO remove once you're doing real-time question gets
	StaticQuestions mQuestions = new StaticQuestions();
	
}
