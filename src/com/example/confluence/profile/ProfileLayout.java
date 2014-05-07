package com.example.confluence.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.confluence.NewsFeedActivity;
import com.example.confluence.R;

public class ProfileLayout extends RelativeLayout {

	public ProfileLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_profile, this, true);
	}
	
	public void setUsername(String name) {
		TextView userName = (TextView) findViewById(R.id.profile_name);
		userName.setText(name);
	}
	
	public void initLanguages() {
		LanguageSelectorLayout knownLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_known_languages); 
		LanguageSelectorLayout desiredLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_desired_languages);
		
		// Load User data		
		String[] knownLangs = NewsFeedActivity.mUser.getKnownLanguages();
		String[] desiredLangs = NewsFeedActivity.mUser.getProfLanguages();
		
		knownLanguages.setTitle("Languages I know:");
		desiredLanguages.setTitle("Languages I want to learn:");
		knownLanguages.initLanguages(knownLangs);
		desiredLanguages.initLanguages(desiredLangs);
	}

}
