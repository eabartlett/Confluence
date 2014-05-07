package com.example.confluence;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.confluence.dbtypes.NewsFeedQuestion;
import com.example.confluence.dbtypes.User;
import com.example.confluence.newsfeed.NewsArrayAdapter;
import com.example.confluence.newsfeed.NewsFeedQuestionView;
import com.example.confluence.profile.LanguageSelectorLayout;
import com.example.confluence.profile.ProfileLayout;
public class NewsFeedActivity extends BaseActivity {

	ConfluenceAPI mApi;
	public static User mUser;
	public SimpleDrawerListener drawerToggle;
	public DrawerLayout mDrawerLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
		mApi = new ConfluenceAPI();
		loadUser("53688688c7a0aa166b8ee5e6");
		
		setEditTextFocus();
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		setUpDrawer();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feed, menu);
		return true;
	}
	
	private class DrawerListener implements android.support.v4.widget.DrawerLayout.DrawerListener {
		@Override
		public void onDrawerClosed(View v) {
			Log.d("DEBUG", "drawer closed");
			loadUser("53688688c7a0aa166b8ee5e6");

			//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		}

		@Override
		public void onDrawerOpened(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDrawerSlide(View arg0, float arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDrawerStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public void setUpDrawer() {
		mDrawerLayout.setDrawerListener((android.support.v4.widget.DrawerLayout.DrawerListener) new DrawerListener());
		
	}

	
	public void setEditTextFocus() {
		View askInput = findViewById(R.id.ask_input);
		askInput.clearFocus();
		
		askInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					callAskQuestionActivity();
				} else {
					findViewById(R.id.ask_input).clearFocus();
				}
			}
		});
		
		askInput.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callAskQuestionActivity();
			}
		});
	}
	
	public void callAskQuestionActivity() {
        Intent askQuestionIntent = new Intent(NewsFeedActivity.this, AskQuestionActivity.class);
        NewsFeedActivity.this.startActivity(askQuestionIntent);
	}
	
	/**
	 * Gets questions from getQuestions method and loads them into the ListView
	 * in the main view. Allows getQuestions to worry about how to get the 
	 * questions.
	 */
	private void loadFeed(NewsFeedQuestion[] questions){
		NewsArrayAdapter questionArray = new NewsArrayAdapter(
				this, R.layout.spinner_row, questions);
		ListView feed = (ListView) findViewById(R.id.news_feed_list);
		feed.setAdapter(questionArray);
		feed.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				ViewGroup viewGroup = (ViewGroup) arg1;
				NewsFeedQuestionView questionView = (NewsFeedQuestionView) viewGroup.getChildAt(0);
				NewsFeedQuestion q = questionView.getQuestion();
				Intent qIntent = new Intent(NewsFeedActivity.this, AnswerActivity.class);
				qIntent.putExtra("id", q.getId());
				//Below this line is all stuff that may or may not be taken out for final functionality
				qIntent.putExtra("question", q.getQuestion());
				qIntent.putExtra("language", q.getLanguage());
				startActivity(qIntent);
				
			}
		});
	}
	
	private void loadUser(String uid){
		RequestUser req = new RequestUser();
		req.execute(uid);
	}
	/**
	 * Gets user's languages and populates the filter by language spinner
	 * with these languages
	 */
	private void loadLanguages(){
		Spinner languages = (Spinner) findViewById(R.id.language_filter);
		String[] langs = getUserLanguages();
		Arrays.sort(langs);
		ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(
				this, R.layout.spinner_row, langs);
		languages.setAdapter(langAdapter);
		languages.setOnItemSelectedListener(new LanguagesFilter());

	}
	
	/**
	 * Loads user information into drawer (i.e. languages, name)
	 */
	private void loadProfile() {
		ProfileLayout profileLayout = (ProfileLayout) findViewById(R.id.navigation_drawer);				
		profileLayout.setUsername(mUser.getFirst());
		profileLayout.initLanguages();
		
		
	}
		
		

	/**
	 * Currently hard coded, would involve an API call in the final version
	 * @return - Returns array of the strings that are the languages a user uses
	 */
	private String[] getUserLanguages(){
		 Log.d("User Language", mUser.getAllUniqueLanguages().toString());
		 return mUser.getAllUniqueLanguages();
	}
	
	private void loadQuestions(String filter){
		new RequestQuestionsSingle().execute(this, filter);
	}
	
	private void loadQuestions(String[] langs){
		new RequestQuestionsArray().execute(this, langs);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	int timesFiltered = 0;
	
	
	/**
	 * Listener for the language filter spinner
	 * @author ebartlett
	 *
	 */
	private class LanguagesFilter implements AdapterView.OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
				NewsFeedActivity.this.loadQuestions((String) ((TextView) arg1).getText());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			//Do nothing
		}

		
	}
	
	private class RequestQuestionsSingle extends AsyncTask<Object, Integer, NewsFeedQuestion[]>{

		@Override
		protected NewsFeedQuestion[] doInBackground(Object... args) {
			final NewsFeedQuestion[] questions = mApi.getQuestionsByLang((String) args[1]);
			((NewsFeedActivity) args[0]).runOnUiThread(new Runnable(){
				
				@Override
				public void run(){
					Log.d("Array", Arrays.toString(questions));
					NewsFeedActivity.this.loadFeed(questions);
				}
			});
			return questions;
			
		}
		
		
	}
	private class RequestQuestionsArray extends AsyncTask<Object, Integer, NewsFeedQuestion[]>{

		@Override
		protected NewsFeedQuestion[] doInBackground(Object... args) {
			final NewsFeedQuestion[] questions = mApi.getQuestionsByLang((String[]) args[1]);
			((NewsFeedActivity) args[0]).runOnUiThread(new Runnable(){
				
				@Override
				public void run(){
					Log.d("Array", Arrays.toString(questions));
					NewsFeedActivity.this.loadFeed(questions);
				}
			});
			return questions;
			
		}
	}
	
	private class RequestUser extends AsyncTask<String, Integer, User>{

		@Override
		protected User doInBackground(String... arg0) {
			mUser = mApi.getUserById(arg0[0]);
			return mUser;
		}
		
		@Override
	    protected void onPostExecute(User user) {
			if (user != null) {
				Log.d("Confluence User", String.valueOf(user));
				loadQuestions(getUserLanguages());
				loadLanguages();
				loadProfile();
			}
	    }
	}

}
