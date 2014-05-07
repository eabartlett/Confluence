package com.example.confluence;


import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * AnswerActiv
 * ity handles all answers associated with a question. 
 * @author brian
 *
 */
public class OpenAnswerActivity extends Activity {

	private String mAnswerId;
	ConfluenceAPI mApi;
	private String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test2.3gp";
	private Button playButton;
	private MediaPlayer mPlayer;
	private CountDownTimer mCountDownTimer; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.confluence.R.layout.activity_open_answer);

		Intent startIntent = getIntent();
		Bundle extras = startIntent.getExtras();

		// Set question fields
		TextView answerView = (TextView) findViewById(R.id.answer_answer_box);
		TextView langView = (TextView) findViewById(R.id.answer_lang_content);
		answerView.setText(extras.getString("answer"));
		langView.setText(extras.getString("language"));
		mAnswerId = extras.getString("id");
		
		mApi = new ConfluenceAPI();
		playButton = (Button) findViewById (R.id.bt_play);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ask_question, menu);
		return true;
	}

	public void playAudioInQuestion(View view) {
		new GetAudioInAnswer().execute("");
		
	}
	
	public class GetAudioInAnswer extends AsyncTask<String, Integer, Boolean>{

		// @Override
		protected Boolean doInBackground(String... params) {
			if (mApi.getAudio(mAnswerId, mFileName, "answer")) {			
				return true;
			}
			Log.d("Confluence ****", "GetAudio failed");
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				mPlayer = new MediaPlayer();
				try {
					playButton.setClickable(false);
					mPlayer.setDataSource(mFileName);
					mPlayer.prepare();
					mPlayer.start();					
				} catch (IllegalArgumentException e) { 
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mCountDownTimer = new CountDownTimer(mPlayer.getDuration(), 1000) {
					public void onTick(long millisUntilFinished) {
						
					}
	
					public void onFinish() {
						if (mPlayer != null) {
							mPlayer.release();
							mPlayer = null;
						}
						playButton.setClickable(true);
					}
				}.start();
			}
	    }
	}
}
