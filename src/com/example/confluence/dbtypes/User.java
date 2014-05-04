package com.example.confluence.dbtypes;

public class User {
	
	public User(String username, String first, String last, String email, String pw, String id){
		mUsername = username;
		mFirstname = first;
		mLastname = last;
		mEmail = email;
		mPassword = pw;
		mId = id;
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
	
	private String mUsername;
	private String mFirstname;
	private String mLastname;
	private String mEmail;
	private String mPassword;
	private String mId;
}
