package com.example.confluence.dbtypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User {
	
	public User(String username, String first, String last, String email, String pw, String id, JSONArray langs){
		mUsername = username;
		mFirstname = first;
		mLastname = last;
		mEmail = email;
		mPassword = pw;
		mId = id;
		mLanguages = langs;
	}
	
	public User(JSONObject user) throws JSONException{
		this(user.getString("username"), user.getString("firstname"), user.getString("lastname"), 
				user.getString("email"), user.getString("pw"), user.getString("_id"), user.getJSONArray("learningLanguages"));
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
	
	public String[] getLanguages(){
		JSONArray json = mLanguages;
		String[] langs = new String[json.length()];
		for(int i = 0; i < langs.length; i++){
			try {
				langs[i] = json.getString(i);
				Log.d("Confluence User Language", langs[i]);
			} catch (JSONException e) {
				Log.d("Confluence Error", "Error parsing user langauges");
			}
		}
		return langs;
	}
	
	private String mUsername;
	private String mFirstname;
	private String mLastname;
	private String mEmail;
	private String mPassword;
	private String mId;
	private JSONArray mLanguages;
}
