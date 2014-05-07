package com.example.confluence.dbtypes;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User {
	
	public User(String username, String first, String last, String email, String pw, String id, JSONArray knownLangs, JSONArray profLangs){
		mUsername = username;
		mFirstname = first;
		mLastname = last;
		mEmail = email;
		mPassword = pw;
		mId = id;
		mKnownLanguages = knownLangs;
		mProfLanguages = profLangs;
	}
	
	public User(JSONObject user) throws JSONException{
		this(user.getString("username"), user.getString("firstname"), user.getString("lastname"), 
				user.getString("email"), user.getString("pw"), user.getString("_id"), user.getJSONArray("learningLanguages"), 
				user.getJSONArray("profLanguages"));
	}
	
	public String getUsername(){
		return mUsername;
	}
	
	public String getFirst(){
		return mFirstname;
	}
	
	public String getLast(){
		return mLastname;
	}
	
	public String getEmail(){
		return mEmail;
	}
	
	public String getPassword(){
		return mPassword;
	}
	
	public String getId(){
		return mId;
	}
	
	public String[] getKnownLanguages() {
		return getLanguages(mKnownLanguages);
	}
	
	public String[] getProfLanguages() {
		return getLanguages(mProfLanguages);
	}
	
	public String[] getAllUniqueLanguages() {
		String[] allLangs =  concatenate(getKnownLanguages(), getProfLanguages());
		HashMap<String, Integer> uniqueLangs = new HashMap<String, Integer>();
		for(int i = 0; i < allLangs.length; i++){
			uniqueLangs.put(allLangs[i], 1);
			Log.d("Confluence User Language", allLangs[i]);
		}
		Set<String> unique_langs = uniqueLangs.keySet(); 
		return unique_langs.toArray(new String[unique_langs.size()]);
	}
	
	private String[] getLanguages(JSONArray json){
		String[] langs = new String[json.length()];
		for(int i = 0; i < langs.length; i++){
			try {
				String singleLang = json.getString(i);
				langs[i] = singleLang.substring(0,1).toUpperCase() + singleLang.substring(1);
				Log.d("Confluence User Language", langs[i]);
			} catch (JSONException e) {
				Log.d("Confluence Error", "Error parsing user langauges");
			}
		}
		return langs;
	}

	private String[] concatenate(String[] array1, String[] array2) {
		String[] allLangs = new String[array1.length + array2.length];
		for (int i = 0; i < array1.length; i++ ) {
			allLangs[i] = array1[i];
		} 
		for (int i=0; i < array2.length; i++)  {
			allLangs[array1.length+i] = array2[i];
		}
		return allLangs;
	}
	
	private String mUsername;
	private String mFirstname;
	private String mLastname;
	private String mEmail;
	private String mPassword;
	private String mId;
	private JSONArray mKnownLanguages;
	private JSONArray mProfLanguages;
}
