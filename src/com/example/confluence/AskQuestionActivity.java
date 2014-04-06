package com.example.confluence;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AskQuestionActivity extends Activity {

    EditText questionEditText;
    Spinner languageSpinner;
    boolean hasRecording;
    private int VOICE_RECORDER_CODE = 1;
    private MediaPlayer mPlayer = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);
		
        questionEditText = (EditText) findViewById(R.id.question_text);


        languageSpinner = (Spinner) findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        

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
        Intent postQuestionIntent = new Intent(AskQuestionActivity.this, PostedQuestionActivity.class);
        postQuestionIntent.putExtra("question", questionText);
        postQuestionIntent.putExtra("language", languageSpinner.getSelectedItem().toString());
        postQuestionIntent.putExtra("hasRecording", hasRecording);
        AskQuestionActivity.this.startActivity(postQuestionIntent);

    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == VOICE_RECORDER_CODE) {
	        if (resultCode == RESULT_OK) {
	        	Toast.makeText(this, "Audio attached", Toast.LENGTH_LONG).show();
	            // Check attached audio
	        	mPlayer = new MediaPlayer();
	            try {
	                mPlayer.setDataSource(data.getExtras().getString(Intent.EXTRA_TEXT));
	                mPlayer.prepare();
	                mPlayer.start();
	                
	            	new CountDownTimer(mPlayer.getDuration(), 1000) {
	            	     public void onTick(long millisUntilFinished) {
	            	    	 
	            	     }

	            	     public void onFinish() {
	            	    	 mPlayer.release();
	            	         mPlayer = null;

	            	     }
	            	}.start();
	            } catch (IOException e) {
	            	
	            }
	        }
	    }
	}
	
    public void addRecording(View v) {
        Intent voiceRecorderIntent = new Intent(AskQuestionActivity.this, VoiceRecorderActivity.class);
        AskQuestionActivity.this.startActivityForResult(voiceRecorderIntent, VOICE_RECORDER_CODE);
    }

}
