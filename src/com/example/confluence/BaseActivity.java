package com.example.confluence;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	    int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
	    TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
	    Typeface appTitleFont = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.otf");
	    Log.d("DEBUG", "font made");
	    if(actionBarTitleView != null){
	        actionBarTitleView.setTypeface(appTitleFont);
	        actionBarTitleView.setTextColor(Color.parseColor("#FFFFFF"));
	        actionBarTitleView.setTextSize(24);
	    }
	    

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}


}
