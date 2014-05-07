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
		LanguageSelectorLayout profLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_known_languages);
		LanguageSelectorLayout learnLanguages = (LanguageSelectorLayout) findViewById(R.id.profile_learn_languages);
		
		// Load User data
		String[] profLangs = NewsFeedActivity.mUser.getProfLanguages();
		String[] learnLangs = NewsFeedActivity.mUser.getLearnLanguages();
		
		profLanguages.setTitle("Languages I know:");
		learnLanguages.setTitle("Languages I want to learn:");
		profLanguages.setIsLearnLayout(false);
		learnLanguages.setIsLearnLayout(true);
		profLanguages.initLanguages(profLangs);
		learnLanguages.initLanguages(learnLangs);
	}

}
