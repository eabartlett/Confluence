package com.example.confluence;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.confluence.profile.LanguageSelectorLayout;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Get UI elements
		LanguageSelectorLayout mKnownLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_known_languages); 
		LanguageSelectorLayout mDesiredLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_desired_languages);
		
		// Load User data		
		String[] knownLangs = NewsFeedActivity.mUser.getKnownLanguages();
		String[] desiredLangs = NewsFeedActivity.mUser.getProfLanguages();
		
		mKnownLanguages.setTitle("Languages I know:");
		mDesiredLanguages.setTitle("Languages I want to learn:");
		mKnownLanguages.initLanguages(knownLangs);
		mDesiredLanguages.initLanguages(desiredLangs);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feed, menu);
		return true;
	}
}
