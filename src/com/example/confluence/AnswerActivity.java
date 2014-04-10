package com.example.confluence;


import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.example.confluence.answers.AnswerArrayAdapter;
import com.example.confluence.answers.AnswerList;

/**
 * AnswerActivity handles all answers associated with a question. 
 * @author brian
 *
 */
public class AnswerActivity extends BaseActivity {

	private ListView listView;
	private EditText answerEditText;
	private ImageButton recordButton;
	private ImageButton playbackButton;
	
	private AnswerList answers;
	private boolean hasAnswers;
	private boolean hasRecording;
	private String recording;
    private int VOICE_RECORDER_CODE = 1;
    private MediaPlayer mPlayer = null;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.confluence.R.layout.activity_answer);
		
		Intent startIntent = getIntent();
		Bundle extras = startIntent.getExtras();
		
		TextView questionView = (TextView) findViewById(R.id.question_phrase_content);
		TextView langView = (TextView) findViewById(R.id.question_lang_content);
		
		questionView.setText(extras.getString("question"));
		langView.setText(extras.getString("language"));
		
		listView = (ListView) findViewById(R.id.answer_list);
		answerEditText = (EditText) findViewById(R.id.answer_question_bar);
		recordButton = (ImageButton) findViewById(R.id.answer_record_audio);
		playbackButton = (ImageButton) findViewById(R.id.answer_playback_audio);
		playbackButton.setClickable(false);
		playbackButton.setEnabled(false);
		answers = new AnswerList();
		
		hasAnswers = extras.getBoolean("hasAnswers");
		hasRecording = extras.getBoolean("hasRecording");
		
		if (hasAnswers) {
			loadAnswersToUI();
		}
		
		if (hasRecording) {
			recording = extras.getString("recording");
		}
		
		setOnPostListener();
		setRecordButtonListener();
		setPlayBackButtonListener();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("onActivityResult");
	    if (requestCode == VOICE_RECORDER_CODE) {
	    	System.out.println("requestCode ok!");
	        if (resultCode == RESULT_OK) {
	        	Toast.makeText(this, "Audio attached", Toast.LENGTH_LONG).show();
	            // Check attached audio
	        	mPlayer = new MediaPlayer();
	            try {
	            	recording = data.getExtras().getString(Intent.EXTRA_TEXT);
		            hasRecording = true;
		            playbackButton.setEnabled(true);
		            playbackButton.setClickable(true);
		            playbackButton.setImageResource(R.drawable.ic_action_play_active);
					playbackButton.setVisibility(View.VISIBLE);
		            

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
        Intent voiceRecorderIntent = new Intent(AnswerActivity.this, VoiceRecorderActivity.class);
        AnswerActivity.this.startActivityForResult(voiceRecorderIntent, VOICE_RECORDER_CODE);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// getMenuInflater().inflate(R.menu.something, menu)
		
		return true;
	}
	
	/**
	 * Loads answers contained in AnswerList answers to the ListView UI.
	 */
	private void loadAnswersToUI() {
		AnswerArrayAdapter answerAdapter = 
				new AnswerArrayAdapter(getApplicationContext(),
						R.layout.activity_answer, 
						answers.getAnswers());
		listView.setAdapter(answerAdapter);
	}
	
	/**
	 * Sets listener on EditText to post a question on the List View
	 */
	private void setOnPostListener() {
		answerEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					handled = true;
					
					String answerText = v.getText().toString();
					answers.addAnswer("Bearly a Group", answerText, hasRecording);
					answerEditText.setText(""); 
					loadAnswersToUI();
					
					playbackButton.setEnabled(false);
					playbackButton.setImageResource(R.drawable.ic_action_play_inactive);
					playbackButton.setVisibility(View.GONE);
				}
				return handled;
			}
			
		});
	}
	
	/**
	 * Sets listener for microphone button to start VoiceRecorderActivity
	 */
	private void setRecordButtonListener() {
		recordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addRecording(v);
			}
		});
	}
	
	/**
	 * Sets listener for playback button to play back audio
	 */
	private void setPlayBackButtonListener() {
		playbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (playbackButton.isClickable() && playbackButton.isEnabled()) {
					mPlayer = new MediaPlayer();
					try {           
						mPlayer.setDataSource(recording);
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
		});
		
	}
}
