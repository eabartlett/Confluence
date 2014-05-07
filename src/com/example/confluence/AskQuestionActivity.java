package com.example.confluence;

import java.text.ParseException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.confluence.answers.AudioFragment;
import com.example.confluence.dbtypes.NewsFeedQuestion;

public class AskQuestionActivity extends BaseActivity {

    EditText questionEditText;
    Spinner languageSpinner;
    boolean hasRecording;
    
    AudioFragment mAudioFooter;
    TextView mTimerText;
    
    Menu menu;
    ConfluenceAPI mApi;
    String mCurrentQID;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);
		
        questionEditText = (EditText) findViewById(R.id.question_text);
        //questionEditText.setScroller(new Scroller(context)); 
        questionEditText.setMaxLines(3);
        questionEditText.setVerticalScrollBarEnabled(true); 
        questionEditText.setMovementMethod(new ScrollingMovementMethod());

		mAudioFooter = (AudioFragment) getFragmentManager().findFragmentById(R.id.audio_footer);

        loadLanguages();
        
        
        mAudioFooter.activateRecordButton(true);
    	mAudioFooter.activatePlayButton(false);
    	//activateAttachButton(false);
    	mApi = new ConfluenceAPI();
    	mCurrentQID = "";
	}
	
	/**
	 * Gets user's languages and populates the filter by language spinner
	 * with these languages
	 * 
	 * Same as in NewsFeedActivity
	 */
	private void loadLanguages(){
		languageSpinner = (Spinner) findViewById(R.id.language_spinner);
		String[] langs = getUserLanguages();
		ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(
				this, R.layout.spinner_row, langs);
		languageSpinner.setAdapter(langAdapter);

	}

	/**
	 * Currently hard coded, would involve an API call in the final version
	 * @return - Returns array of the strings that are the languages a user uses
	 * 
	 * Same as in NewsFeedActivity
	 */
	private String[] getUserLanguages(){
		String[] langs = new String[5];

		langs[0] = "Select a language";
			//a different prompt than in newsfeed activity
		langs[1] = "english";
		langs[2] = "french";
		langs[3] = "german";
		langs[4] = "spanish";
		
		return langs;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ask_question, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        if (id == R.id.action_post) {
        	String questionText = questionEditText.getText().toString();
        	if (questionText != "") {
        		postQuestion(questionText);
        	} else {
        		//Toast
        	}
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void postQuestion(String questionText) {
		String chosenLang = languageSpinner.getSelectedItem().toString();
		
		if (chosenLang.equals("Select a language")) {
			Toast toast = Toast.makeText(getApplicationContext(), "Please select a language", Toast.LENGTH_SHORT);  
			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
			toast.show();
		} else if (!mAudioFooter.hasRecording()){
			Toast toast = Toast.makeText(getApplicationContext(), "Please add a recording", Toast.LENGTH_SHORT);  
			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
			toast.show();
		} else {
	        /*Intent postQuestionIntent = new Intent(AskQuestionActivity.this, PostedQuestionActivity.class);
	        postQuestionIntent.putExtra("question", questionText);
	        postQuestionIntent.putExtra("language", languageSpinner.getSelectedItem().toString());
	        postQuestionIntent.putExtra("hasRecording", hasRecording);
	        postQuestionIntent.putExtra("recording", mAudioFooter.getAudioFilePath() ); */
	
			// Need to change this - 
			// Maybe maintain a count var in the user object.
			String user = NewsFeedActivity.mUser.getId();//"53688688c7a0aa166b8ee5e6";
			NewsFeedQuestion Question;
			try {
				Question = new NewsFeedQuestion(
						"", 
						chosenLang, 
						questionText, 
						mAudioFooter.getAudioFilePath(), 
						user);
				new PostQuestion().execute(Question);
				new PostAudioInQuestion().execute("");
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			Intent postQuestionIntent = new Intent(AskQuestionActivity.this, NewsFeedActivity.class);
	        AskQuestionActivity.this.startActivity(postQuestionIntent);
		}
    }

	private class PostQuestion extends AsyncTask<NewsFeedQuestion, Integer, JSONObject>{

		@Override
		protected JSONObject doInBackground(NewsFeedQuestion... Question) {
			return mApi.postQuestion(Question[0]);
		}
		
		@Override
	    protected void onPostExecute(JSONObject question) {
			if (question != null) {
				//Log.d("Confluence User", question.toString());
				mCurrentQID = question.optString("_id");
			}
	    }
	}
	
	private class PostAudioInQuestion extends AsyncTask<String, Integer, NewsFeedQuestion>{

		@Override
		protected NewsFeedQuestion doInBackground(String... params) {
			return mApi.postQuestionAudio(mAudioFooter.getAudioFilePath(), mCurrentQID);
		}
		
		@Override
	    protected void onPostExecute(NewsFeedQuestion question) {
			if (question == null) {
				Toast.makeText(AskQuestionActivity.this, "Couldn't post audio", Toast.LENGTH_LONG).show();
			}
	    }
	}
}
