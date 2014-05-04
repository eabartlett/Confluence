package com.example.confluence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.example.confluence.answers.Answer;
import com.example.confluence.dbtypes.NewsFeedQuestion;
import com.example.confluence.dbtypes.User;


public class ConfluenceAPI {

	final static String SERVER = "http://eabartlett.com/%s";
	public final static String SUCCESS = "Success";
	// Creating HTTP client
	private DefaultHttpClient mHttpClient = new DefaultHttpClient();

	public JSONObject postQuestion(NewsFeedQuestion q){
		// Building post parameters
		// key and value pair
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("question", q.getQuestion()));
		nameValuePair.add(new BasicNameValuePair("user", q.getUser()));
		nameValuePair.add(new BasicNameValuePair("lang", q.getLanguage()));

		//construct URL endpoint
		String endpoint = String.format(SERVER, "api/question");

		return postData(endpoint, nameValuePair);
	}

	public JSONObject postAnswer(Answer a){
		// Building post parameters
		// key and value pair
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("answer", a.getText()));
		nameValuePair.add(new BasicNameValuePair("user", a.getUserId()));
		nameValuePair.add(new BasicNameValuePair("lang", a.getLanguage()));

		//construct URL endpoint
		String endpoint = String.format(SERVER, "api/answer");

		return postData(endpoint, nameValuePair);
	}

	public JSONObject postUser(User u){

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);

		//Build data parameters to send to server
		nameValuePair.add(new BasicNameValuePair("username", u.getUsername()));
		nameValuePair.add(new BasicNameValuePair("email", u.getEmail()));
		nameValuePair.add(new BasicNameValuePair("pw", u.getPassword()));
		nameValuePair.add(new BasicNameValuePair("firstname", u.getFirst()));
		nameValuePair.add(new BasicNameValuePair("lastname", u.getLast()));

		String endpoint = String.format(SERVER, "api/user");

		return postData(endpoint, nameValuePair);
	}

	public JSONObject postData(String endpoint, List<NameValuePair> data){
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(endpoint);

		// Url Encoding the POST parameters
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(data));
		} catch (UnsupportedEncodingException e) {
			// writing error to Log
			e.printStackTrace();
		}

		// Making HTTP Request
		try {
			HttpResponse response = mHttpClient.execute(httpPost);
			return getJSONObject(response);
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public NewsFeedQuestion[] getQuestionsByLang(String lang){
		String url = String.format(SERVER, "api/question?%s=%s");
		url = contstructGetUrl(url, "lang", lang);
		JSONArray res = (JSONArray) getRequest(url, true);
		NewsFeedQuestion[] questions = new NewsFeedQuestion[res.length()];
		for(int i = 0; i < res.length(); i++){
			try {
				JSONObject question = res.getJSONObject(i);
				questions[i] = new NewsFeedQuestion(question);
			} catch (JSONException e) {
				Log.d("Error", e.getMessage());
			}
		}
		return questions;
	}
	
	public JSONObject getQuestionById(String qid){
		String url = String.format(SERVER, "api/question?%s=%s");
		url = contstructGetUrl(url, "id", qid);
		return (JSONObject) getRequest(url, false);
	}
	
	public Object getRequest(String endpoint, boolean isArray){
		HttpGet get = new HttpGet(endpoint);
		try{
			HttpResponse response = mHttpClient.execute(get);
			Object res = null;
			if(isArray){
				res = getJSONArray(response);
			}else{
				res = getJSONObject(response);	
			}
			return res;
		} catch(JSONException e){
			Log.d("Confluence Error", e.getMessage());
		} catch(IOException e){
			Log.d("Confluence Error", e.getMessage());
		}
		return null;
	}

	private String contstructGetUrl(String base, String key, String val){
		String url = String.format(base, key, val);
		return url;
	}

	/**
	 * @param res - httpresponse returned from a POST or GET
	 * @throws IllegalStateException 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject getJSONObject(HttpResponse res) throws 
	UnsupportedEncodingException, IllegalStateException, IOException, JSONException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}
		JSONTokener tokener = new JSONTokener(builder.toString());
		JSONObject finalResult = new JSONObject(tokener);
		return finalResult;
	}

	/**
	 * 
	 * @param res - httpresponse returned from a POST or GET
	 * @return - JSONArray of data
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private JSONArray getJSONArray(HttpResponse res) throws 
	JSONException, UnsupportedEncodingException, IllegalStateException, IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}
		JSONTokener tokener = new JSONTokener(builder.toString());
		JSONArray finalResult = new JSONArray(tokener);
		return finalResult;
	}
}
