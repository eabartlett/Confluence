package com.example.confluence;

import android.content.Intent;
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
import com.example.confluence.answers.AudioFragment.OnTimerStarted;

public class AskQuestionActivity extends BaseActivity implements OnTimerStarted{

    EditText questionEditText;
    Spinner languageSpinner;
    boolean hasRecording;
    private int VOICE_RECORDER_CODE = 1;
    
    AudioFragment mAudioFooter;
    TextView mTimerText;
    
    Menu menu;
    
    
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
		mTimerText = (TextView) findViewById(R.id.txt_timer);

        loadLanguages();
        
        
        mAudioFooter.activateRecordButton(true);
    	mAudioFooter.activatePlayButton(false);
    	//activateAttachButton(false);

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
		langs[1] = "English";
		langs[2] = "French";
		langs[3] = "German";
		langs[4] = "Spanish";
		
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
	        Intent postQuestionIntent = new Intent(AskQuestionActivity.this, PostedQuestionActivity.class);
	        postQuestionIntent.putExtra("question", questionText);
	        postQuestionIntent.putExtra("language", languageSpinner.getSelectedItem().toString());
	        postQuestionIntent.putExtra("hasRecording", hasRecording);
	        postQuestionIntent.putExtra("recording", mAudioFooter.getAudioFilePath() );
	
	        AskQuestionActivity.this.startActivity(postQuestionIntent);
		}
        
        
		/*Intent postQuestionIntent = new Intent(AskQuestionActivity.this, AnswerActivity.class);
		postQuestionIntent.putExtra("question", questionText);
        postQuestionIntent.putExtra("language", languageSpinner.getSelectedItem().toString());
        postQuestionIntent.putExtra("hasAnswers", false); 
        	// ^ for interactive prototype
        postQuestionIntent.putExtra("hasRecording", hasRecording);
        postQuestionIntent.putExtra("recording", recording);
		AskQuestionActivity.this.startActivity(postQuestionIntent);*/


    }

	@Override
	public void setCountdownText(String time) {
		// TODO Auto-generated method stub
		mTimerText.setText(time);		
	}
	
    /*public void addRecording(View v) {
        Intent voiceRecorderIntent = new Intent(AskQuestionActivity.this, VoiceRecorderActivity.class);
        AskQuestionActivity.this.startActivityForResult(voiceRecorderIntent, VOICE_RECORDER_CODE);
    }*/
    
}
