package com.example.confluence;

import com.example.confluence.newsfeed.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
		ListView feed = (ListView) findViewById(R.id.news_feed_list);
		feed.setAdapter(questionArray);
		feed.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NewsFeedQuestion q = ((NewsFeedQuestionView) arg1).getQuestion();
				Intent qIntent = new Intent(NewsFeedActivity.this, PostedQuestionActivity.class);
				qIntent.putExtra("id", q.getId());
				//Below this line is all stuff that may or may not be taken out for final functionality
				qIntent.putExtra("question", q.getQuestion());
				qIntent.putExtra("language", q.getLanguageTo());
				qIntent.putExtra("type", q.getQuestionType());
				startActivity(qIntent);
				
			}
		});
	}

	/**
	 * If using api, this would use api calls to get the correct array of questions for
	 * the feed, but as of now it is just using StaticQuestions.getQuestions() w/o the filter
	 * @return - Questions for feed (hard-coded for interactive prototype)
	 */
	private NewsArrayAdapter<NewsFeedQuestion> getQuestions() {
		return new NewsArrayAdapter<NewsFeedQuestion>(this, R.layout.activity_news_feed, mQuestions.getQuestions());

	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        if (id == R.id.action_ask) {
            Intent askQuestionIntent = new Intent(NewsFeedActivity.this, AskQuestionActivity.class);
            NewsFeedActivity.this.startActivity(askQuestionIntent);

            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//TODO remove once you're doing real-time question gets
	StaticQuestions mQuestions = new StaticQuestions();
	
}
