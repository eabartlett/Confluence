package com.example.confluence;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.confluence.profile.LanguageSelectorLayout;

public class ProfileActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Get UI elements
		LanguageSelectorLayout knownLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_known_languages); 
		LanguageSelectorLayout desiredLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_desired_languages);
		
		List<String> langs = Arrays.asList(LanguageSelectorLayout.LANGUAGES);
		
		knownLanguages.addLanguage(langs.get(0));
		knownLanguages.setTitle("Languages I know:");
		desiredLanguages.addLanguage(langs.get(1));
		desiredLanguages.addLanguage(langs.get(2));
		desiredLanguages.setTitle("Languages I want to learn:");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feed, menu);
		return true;
	}
}
