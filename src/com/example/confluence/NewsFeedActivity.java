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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class NewsFeedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
		loadFeed(null);
		loadLanguages();
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
	private void loadFeed(String filter){
		NewsArrayAdapter<NewsFeedQuestion> questionArray = getQuestions(filter);
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
				qIntent.putExtra("language", q.getLanguage());
				startActivity(qIntent);
				
			}
		});
	}
	
	/**
	 * Gets user's languages and populates the filter by language spinner
	 * with these languages
	 */
	private void loadLanguages(){
		Spinner languages = (Spinner) findViewById(R.id.language_filter);
		String[] langs = getUserLanguages();
		ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(
				this, R.layout.textview, langs);
		languages.setAdapter(langAdapter);
		languages.setOnItemSelectedListener(new LangaugesFilter());

	}

	/**
	 * Currently hard coded, would involve an API call in the final version
	 * @return - Returns array of the strings that are the languages a user uses
	 */
	private String[] getUserLanguages(){
		String[] langs = new String[5];

		langs[0] = "All Languages";
		langs[1] = "English";
		langs[2] = "French";
		langs[3] = "German";
		langs[4] = "Spanish";
		
		return langs;
	}
	/**
	 * If using api, this would use api calls to get the correct array of questions for
	 * the feed, but as of now it is just using StaticQuestions.getQuestions() w/o the filter
	 * @return - Questions for feed (hard-coded for interactive prototype)
	 */
	private NewsArrayAdapter<NewsFeedQuestion> getQuestions(String filter) {
		if(filter == null){
			return new NewsArrayAdapter<NewsFeedQuestion>(
					this, R.layout.activity_news_feed, mQuestions.getQuestions());
		}else{
			return new NewsArrayAdapter<NewsFeedQuestion>(
					this, R.layout.activity_news_feed, mQuestions.getQuestions(filter));	
		}

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
	int timesFiltered = 0;
	
	
	/**
	 * Listener for the language filter spinner
	 * @author ebartlett
	 *
	 */
	private class LangaugesFilter implements AdapterView.OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(arg2 == 0){
				NewsFeedActivity.this.loadFeed(null);
			}else{
				NewsFeedActivity.this.loadFeed((String) ((TextView) arg1).getText());
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			//Do nothing
		}

		
	}
	
}
