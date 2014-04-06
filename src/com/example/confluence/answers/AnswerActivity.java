package com.example.confluence.answers;


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

import com.example.confluence.R;

/**
 * AnswerActivity handles all answers associated with a question. 
 * @author brian
 *
 */
public class AnswerActivity extends Activity {

	private ListView listView;
	private EditText answerEditText;
	private ImageButton recordButton;
	private StaticAnswers answers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.confluence.R.layout.activity_answer);
		
		Intent startIntent = getIntent();
		Bundle extras = startIntent.getExtras();
		
		TextView phraseView = (TextView) findViewById(R.id.question_phrase_content);
		TextView langView = (TextView) findViewById(R.id.question_lang_content);
		TextView tagView = (TextView) findViewById(R.id.question_tag_content);
		
		listView = (ListView) findViewById(R.id.answer_list);
		answerEditText = (EditText) findViewById(R.id.answer_question_bar);
		recordButton = (ImageButton) findViewById(R.id.answer_record_audio);
		
		
		setAnswerListener();
		setRecordButtonListener();
		loadAnswers();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// getMenuInflater().inflate(R.menu.something, menu)
		
		return true;
	}
	
	private void loadAnswers() {
		answers = new StaticAnswers();
		AnswerArrayAdapter answerAdapter = 
				new AnswerArrayAdapter(getApplicationContext(),
						R.layout.activity_answer, 
						answers.getAnswers());
		listView.setAdapter(answerAdapter);
	}
	
	/**
	 * Sets listener on EditText to add question to ListView.
	 */
	private void setAnswerListener() {
		answerEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					// addView to adapter 
					String answerText = v.getText().toString();
					answerEditText.setText(""); 
					Answer newAnswer = new Answer("Bearly a Group",
							answerText,
							null);
					answers.addAnswer(newAnswer);
					AnswerArrayAdapter answerAdapter = 
							new AnswerArrayAdapter(getApplicationContext(),
									R.layout.activity_answer, 
									answers.getAnswers());
					listView.setAdapter(answerAdapter);
					handled = true;
				}
				return handled;
			}
			
		});
	}
	
	private void setRecordButtonListener() {
		recordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				Intent audioRecordIntent = new Intent(this, VoiceRecorderActivity.class);
				startActivity(audioRecordIntent);*/
			}
			
		});
	}
}
