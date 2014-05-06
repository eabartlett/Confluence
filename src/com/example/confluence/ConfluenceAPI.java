package com.example.confluence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.example.confluence.dbtypes.Answer;
import com.example.confluence.dbtypes.NewsFeedQuestion;
import com.example.confluence.dbtypes.User;


public class ConfluenceAPI {

	final static String SERVER = "http://eabartlett.com/%s";
	public final static String SUCCESS = "Success";
	// Creating HTTP client
	private DefaultHttpClient mHttpClient = new DefaultHttpClient();
	
	public boolean getAudio(String id, String filepath, String type){
		String endpoint = constructGetUrl(String.format(SERVER, "audio"), type, id);
		HttpResponse res = (HttpResponse) getRequest(endpoint, false, true);
		File file = new File(filepath);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			InputStream is = res.getEntity().getContent();
			
			//Taken from stackoverflow: http://stackoverflow.com/questions/19733612/how-to-download-an-httpresponse-into-a-file
			int read = 0;
			byte[] buffer = new byte[32768];
			while( (read = is.read(buffer)) > 0) {
			  fos.write(buffer, 0, read);
			}
			return true;
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public NewsFeedQuestion postQuestionAudio(String filepath, String qid){
		String endpoint = String.format(SERVER, "api/audio");
		try {
			NewsFeedQuestion q = new NewsFeedQuestion(postMultiPartData(endpoint, filepath, "question", qid));
			return q;
		} catch (JSONException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Answer postAnswerAudio(String filepath, String aid){
		String endpoint = String.format(SERVER, "api/audio");
		try {
			Answer a = new Answer(postMultiPartData(endpoint, filepath, "answer", aid));
			return a;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public User addLangUser(String uid, String lang){
		List<NameValuePair> vals = new ArrayList<NameValuePair>(2);
		vals.add(new BasicNameValuePair("id", uid));
		vals.add(new BasicNameValuePair("lang", lang));
		String endpoint = String.format(SERVER, "api/user/lang");
		
		try {
			return new User(postKVData(endpoint, vals));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public JSONObject postQuestion(NewsFeedQuestion q){
		// Building post parameters
		// key and value pair
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
		nameValuePair.add(new BasicNameValuePair("question", q.getQuestion()));
		nameValuePair.add(new BasicNameValuePair("user", q.getUser()));
		nameValuePair.add(new BasicNameValuePair("lang", q.getLanguage()));

		//construct URL endpoint
		String endpoint = String.format(SERVER, "api/question");

		return postKVData(endpoint, nameValuePair);
	}

	public JSONObject postAnswer(Answer a){
		// Building post parameters
		// key and value pair
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
		nameValuePair.add(new BasicNameValuePair("answer", a.getText()));
		nameValuePair.add(new BasicNameValuePair("user", a.getUserId()));
		nameValuePair.add(new BasicNameValuePair("lang", a.getLanguage()));

		//construct URL endpoint
		String endpoint = String.format(SERVER, "api/answer");

		return postKVData(endpoint, nameValuePair);
	}

	public User postUser(User u){

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);

		//Build data parameters to send to server
		nameValuePair.add(new BasicNameValuePair("username", u.getUsername()));
		nameValuePair.add(new BasicNameValuePair("email", u.getEmail()));
		nameValuePair.add(new BasicNameValuePair("pw", u.getPassword()));
		nameValuePair.add(new BasicNameValuePair("firstname", u.getFirst()));
		nameValuePair.add(new BasicNameValuePair("lastname", u.getLast()));

		String endpoint = String.format(SERVER, "api/user");

		try {
			return new User(postKVData(endpoint, nameValuePair));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("Error", e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private JSONObject postMultiPartData(String endpoint, String filename, String type,String id){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(endpoint);
		try {
			MultipartEntity mpEntity  = new MultipartEntity();
			mpEntity.addPart(type, new StringBody(id));
			File file = new File(filename);
			mpEntity.addPart("audio", (ContentBody) new FileBody(file, "audio/mpeg"));
			post.setEntity(mpEntity);
			HttpResponse res = client.execute(post);
			return getJSONObject(res);
		} catch (IOException e) {
			Log.d("Confluence Error", "Failed posting data to server");
		} catch (IllegalStateException e) {
			Log.d("Confluence Error", "Failed parsing JSON response from server");
		} catch (JSONException e) {
			Log.d("Confluence Error", "Failed parsing JSON response from server");
		}
		return null;
	}

	private JSONObject postKVData(String endpoint, List<NameValuePair> data){
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

	public NewsFeedQuestion[] getQuestionsByLang(String[] langs){
		ArrayList<NewsFeedQuestion[]> questions = new ArrayList<NewsFeedQuestion[]>();
		for(String lang: langs){
			questions.add(getQuestionsByLang(lang));
		}
		int l = 0;
		for(NewsFeedQuestion[] arr: questions){
			l += arr.length;
		}
		NewsFeedQuestion[] res = new NewsFeedQuestion[l];
		int i = 0;
		for(NewsFeedQuestion[] arr: questions){
			for(NewsFeedQuestion q: arr){
				res[i] = q;
				i++;
			}
		}
		return res;
	}
	public NewsFeedQuestion[] getQuestionsByLang(String lang){
		String url = String.format(SERVER, "api/question?%s=%s");
		url = constructGetUrl(url, "lang", lang);
		JSONArray res = (JSONArray) getRequest(url, true, false);
		NewsFeedQuestion[] questions = new NewsFeedQuestion[res.length()];
		for(int i = 0; i < res.length(); i++){
			try {
				JSONObject question = res.getJSONObject(i);
				questions[i] = new NewsFeedQuestion(question);
			} catch (JSONException e ) {
				Log.d("Error", e.getMessage());
			} catch (ParseException e) {
				Log.d("Error", e.getMessage());
			}
		}
		return questions;
	}
	public NewsFeedQuestion[] getQuestionsByUser(String uid){
		String url = String.format(SERVER, "api/question?%s=%s");
		url = constructGetUrl(url, "user", uid);
		JSONArray res = (JSONArray) getRequest(url, true, false);
		NewsFeedQuestion[] questions = new NewsFeedQuestion[res.length()];
		for(int i = 0; i < res.length(); i++){
			try {
				JSONObject question = res.getJSONObject(i);
				questions[i] = new NewsFeedQuestion(question);
			} catch (JSONException e ) {
				Log.d("Error", e.getMessage());
			} catch (ParseException e) {
				Log.d("Error", e.getMessage());
			}
		}
		return questions;
	}

	public NewsFeedQuestion getQuestionById(String qid){
		String url = String.format(SERVER, "api/question?%s=%s");
		url = constructGetUrl(url, "id", qid);
		try {
			return new NewsFeedQuestion((JSONObject) getRequest(url, false, false));
		} catch (JSONException e) {
			Log.d("Error", e.getMessage());
		} catch (ParseException e) {
			Log.d("Error", e.getMessage());
		}
		return null;
	}

  public Answer getAnswerById(String aid){
		String url = String.format(SERVER, "api/answer?%s=%s");
		url = constructGetUrl(url, "id", aid);
		try {
			return new Answer((JSONObject) getRequest(url, false, false));
		} catch (JSONException e) {
			Log.d("Error", e.getMessage());
		}
		return null;
  }

  public Answer[] getAnswersByQuestion(String qid){
		String url = String.format(SERVER, "api/answer?%s=%s");
		url = constructGetUrl(url, "qid", qid);
		try {
			JSONArray data = (JSONArray) getRequest(url, true, false);
      Answer[] answers = new Answer[data.length()];
      for(int i = 0; i < answers.length; i++){
        answers[i] = new Answer(data.getJSONObject(i));
      }
      return answers;
		} catch (JSONException e) {
			Log.d("Error", e.getMessage());
		}
		return null;
  }

  public Answer[] getAnswersByUser(String uid){
		String url = String.format(SERVER, "api/answer?%s=%s");
		url = constructGetUrl(url, "user", uid);
		try {
			JSONArray data = (JSONArray) getRequest(url, true, false);
      Answer[] answers = new Answer[data.length()];
      for(int i = 0; i < answers.length; i++){
        answers[i] = new Answer(data.getJSONObject(i));
      }
      return answers;
		} catch (JSONException e) {
			Log.d("Error", e.getMessage());
		}
		return null;
  }

	public User getUserById(String uid){
		String url = String.format(SERVER, "api/user?%s=%s");
		url = constructGetUrl(url, "id", uid);
		try {
			JSONObject user = (JSONObject) getRequest(url, false, false);
			if (user != null)
				return new User(user);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public Object getRequest(String endpoint, boolean isArray, boolean isDownload){
		HttpGet get = new HttpGet(endpoint);
		try{
			HttpResponse response = mHttpClient.execute(get);
			Object res = null;
			if(isArray){
				res = getJSONArray(response);
			}else if(isDownload){
				return response;
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

	private String constructGetUrl(String base, String key, String val){
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
			Log.d("Confluence JSONObject", line);
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
