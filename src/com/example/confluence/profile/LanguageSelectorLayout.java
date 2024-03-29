package com.example.confluence.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.confluence.ConfluenceAPI;
import com.example.confluence.NewsFeedActivity;
import com.example.confluence.R;
import com.example.confluence.dbtypes.User;

public class LanguageSelectorLayout extends RelativeLayout {

	public static String[] LANGUAGES = {"English", "Spanish", "French", "German", "Swedish", "Russian"};
	public static List<String> LANG_LIST = Arrays.asList(LanguageSelectorLayout.LANGUAGES);
	
	private ArrayList<String> mLanguages = new ArrayList<String>();
	private boolean mIsLearnLayout = false;
	
	private ConfluenceAPI mApi;
	private AutoCompleteTextView mLanguageInput;

	//TODO: remove checks to case when adding/removing languages
	
	public LanguageSelectorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.language_selection_view, this, true);
		
		mApi = new ConfluenceAPI();
		
		// Get references to UI elements
		mLanguageInput = (AutoCompleteTextView) findViewById(R.id.language_selector);
		
		// Set default elements for AutoCompleteTextView
		ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_list_item_1, LanguageSelectorLayout.LANGUAGES);
		mLanguageInput.setAdapter(languageAdapter);

		
		// Set listener for add Button
		mLanguageInput.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				AddLanguage req =  new AddLanguage();
				String lang = (String)((TextView) v).getText();
				if (!(mLanguages.contains(lang.toLowerCase()) || mLanguages.contains(lang.toUpperCase()) || 
						mLanguages.contains(lang))) {
					req.execute(lang);
				}
			}
			
		});
	}
	
	/**
	 * Sets mIsLearnLayout variable used to determine if learning vs professional language layout.
	 * Used to determine which api call to make
	 * @param isLearn
	 */
	public void setIsLearnLayout(boolean isLearn) {
		mIsLearnLayout = isLearn;
	}
		
	/**
	 * Set title of LanguageSelectorLayout. Used to distinguish between 'Known' and 'Learning' languages
	 * @param title String to use as title
	 */
	public void setTitle(String title) {
		TextView titleView = (TextView) findViewById(R.id.language_section_title);
		titleView.setText(title);
	}

	public void initLanguages(String[] langs) {
		for (int i=0; i < langs.length; i++) {
			addLanguage(langs[i]);
		} 
	}
	
	/**
	 * Adds language view to list of languages 
	 * @param lang String of lang to add
	 * @return boolean if adding succeeded
	 * TODO: check if language exists already
	 */
	@SuppressLint("DefaultLocale")
	public boolean addLanguage(String lang) {
		if (mLanguages.contains(lang.toLowerCase()) || mLanguages.contains(lang.toUpperCase()) || 
				mLanguages.contains(lang)) {
			return false;
		}
		
		// TODO: fix very slow code
		mLanguages.add(lang);
		Collections.sort(mLanguages);
		int index = mLanguages.indexOf(lang);
		
		LinearLayout languageList = (LinearLayout) findViewById(R.id.language_list);
		final LanguageRemoveItem newRow = new LanguageRemoveItem(getContext(), lang);

		newRow.findViewById(R.id.language_remove_button).setOnClickListener( 
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String lang = newRow.getText();
						DeleteLanguage req = new DeleteLanguage();
						
						if (mLanguages.contains(lang.toLowerCase()) || mLanguages.contains(lang.toUpperCase()) || 
								mLanguages.contains(lang)) {
							req.execute(lang);
						}
						
					}
				});
		
		languageList.addView(newRow, index);

		mLanguageInput.setText("");

		return true;
	}
	
	/**
	 * Removes language from list of languages
	 * @param lang String of language to remove
	 * @return boolean if remove succeeded
	 */
	public boolean removeLanguage(String lang) {
		int index = mLanguages.indexOf(lang);
		if (index == -1) {
			return false;
		}
		mLanguages.remove(index);
		LinearLayout languageList = (LinearLayout) findViewById(R.id.language_list);
		languageList.removeViewAt(index);
		return true;
	}
	
	private class LanguageListAdapter extends ArrayAdapter<String> {
		
		public LanguageListAdapter(Context context, int resource, List<String> langs) {
			super(context, resource, langs);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String language = (String) getItem(position);
			return new LanguageAddItem(getContext(), language);
		}
		
	}
	
	private class LanguageAddItem extends RelativeLayout {

		public LanguageAddItem(Context context, String language) {
			super(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.language_add_elem, this, true);	
			
			TextView langText = (TextView) findViewById(R.id.add_language_text);
			langText.setText(language);
		}
		
		public String getText() {
			TextView langText = (TextView) findViewById(R.id.add_language_text);
			return (String) langText.getText();
		}
	}
	
	private class LanguageRemoveItem extends RelativeLayout {

		public LanguageRemoveItem(Context context, String language) {
			super(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.language_remove_elem, this, true);	
			
			TextView langText = (TextView) findViewById(R.id.remove_language_text);
			langText.setText(language);
		}
		
		public String getText() {
			TextView langText = (TextView) findViewById(R.id.remove_language_text);
			return (String) langText.getText();
		}
	}
	
	private class AddLanguage extends AsyncTask<String, Integer, User> {

		String mLang;
		
		@Override
		protected User doInBackground(String... params) {
			// TODO Auto-generated method stub
			String lang = params[0];
			User result;
			if (mIsLearnLayout) {
				result = mApi.addLearnLangUser(NewsFeedActivity.mUser.getId(), lang);
			} else { 
				result = mApi.addProfLangUser(NewsFeedActivity.mUser.getId(), lang);
			}
			if (result == null) {
				Toast msg = Toast.makeText(getContext(), "Error with servers. Language not added.", Toast.LENGTH_LONG);
				msg.show();
			} else {
				mLang = lang;
			}
			return result; 
		}
		
		@Override
	    protected void onPostExecute(User user) {
			if (user != null && mLang!= null) {
				addLanguage(mLang);
			}
	    }
	}
	
	private class DeleteLanguage extends AsyncTask<String, Integer, User> {

		String mLang;
		
		@Override
		protected User doInBackground(String... params) {
			// TODO Auto-generated method stub
			String lang = params[0];
			User result;
			if (mIsLearnLayout) {
				result = mApi.delLearnLangUser(NewsFeedActivity.mUser.getId(), lang);
			} else { 
				result = mApi.delProfLangUser(NewsFeedActivity.mUser.getId(), lang);
			}
			if (result == null) {
				Toast msg = Toast.makeText(getContext(), "Error with servers. Language not removed.", Toast.LENGTH_LONG);
				msg.show();
			} else {
				mLang = lang;
			}
			return result; 
		}
		
		@Override
	    protected void onPostExecute(User user) {
			if (user != null && mLang!= null) {
				removeLanguage(mLang);
			}
	    }
	}
}
