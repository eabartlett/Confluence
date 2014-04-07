package com.example.confluence;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
	private AnswerList answers;
	private boolean hasAnswers;
	private boolean hasRecording;
	private String recording;
	
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
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// insert handling for getting file path and attaching to right answer.
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
					String answerText = v.getText().toString();
					answers.addAnswer("Bearly a Group", answerText);
					answerEditText.setText(""); 
					loadAnswersToUI();
					handled = true;
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
				Intent audioRecordIntent = new Intent(getApplicationContext(), VoiceRecorderActivity.class);
				startActivity(audioRecordIntent);
			}
		});
	}
}
