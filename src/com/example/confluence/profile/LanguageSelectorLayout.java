package com.example.confluence.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
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

import com.example.confluence.R;

public class LanguageSelectorLayout extends RelativeLayout {

	public static String[] LANGUAGES = {"English", "Spanish", "French"};
	public static List<String> LANG_LIST = Arrays.asList(LanguageSelectorLayout.LANGUAGES);
	
	private ArrayList<String> languages = new ArrayList<String>();
	
	public LanguageSelectorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.language_selection_view, this, true);
		
		// Set default languages user can add.
		/*LanguageListAdapter languageAdapter = new LanguageListAdapter(context,
				android.R.layout.simple_dropdown_item_1line, LanguageSelectorLayout.LANG_LIST);*/
		ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, LanguageSelectorLayout.LANG_LIST);
		AutoCompleteTextView languageInput = (AutoCompleteTextView) findViewById(R.id.language_selector);
		languageInput.setAdapter(languageAdapter);
		
		// Set listener for add Button
		languageInput.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				System.out.println("Itemclicked!");
				String lang = (String)((TextView) v).getText();
				addLanguage(lang); 
			}
			
		});
	}
	
	/**
	 * Set title of LanguageSelectorLayout. Used to distinguish between 'Known' and 'Learning' languages
	 * @param title String to use as title
	 */
	public void setTitle(String title) {
		TextView titleView = (TextView) findViewById(R.id.language_section_title);
		titleView.setText(title);
	}

	/**
	 * Adds language view to list of languages 
	 * @param lang String of lang to add
	 * @return boolean if adding succeeded
	 * TODO: check if language exists already
	 */
	public boolean addLanguage(String lang) {
		if (languages.contains(lang)) {
			return false;
		}
		
		// TODO: fix very slow code
		languages.add(lang);
		Collections.sort(languages);
		int index = languages.indexOf(lang);
		
		LinearLayout languageList = (LinearLayout) findViewById(R.id.language_list);
		final LanguageRemoveItem newRow = new LanguageRemoveItem(getContext(), lang);

		newRow.findViewById(R.id.language_remove_button).setOnClickListener( 
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String lang = newRow.getText();
						removeLanguage(lang);
					}
				});
		
		languageList.addView(newRow, index);
		return true;
	}
	
	/**
	 * Removes language from list of languages
	 * @param lang String of language to remove
	 * @return boolean if remove succeeded
	 */
	public boolean removeLanguage(String lang) {
		int index = languages.indexOf(lang);
		if (index == -1) {
			return false;
		}
		languages.remove(index);
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
}
