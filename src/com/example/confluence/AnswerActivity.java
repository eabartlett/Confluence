package com.example.confluence;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.confluence.answers.AnswerArrayAdapter;
import com.example.confluence.answers.AnswerList;
import com.example.confluence.answers.AudioFragment;

/**
 * AnswerActivity handles all answers associated with a question. 
 * @author brian
 *
 */
public class AnswerActivity extends BaseActivity {

	private ListView listView;
	private EditText answerEditText;
	private AudioFragment mAudioFooter;

	private String mAnswerRecordingPath;
	private AnswerList answers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.confluence.R.layout.activity_answer);

		Intent startIntent = getIntent();
		Bundle extras = startIntent.getExtras();

		// Set question fields
		TextView questionView = (TextView) findViewById(R.id.question_phrase_content);
		TextView langView = (TextView) findViewById(R.id.question_lang_content);
		questionView.setText(extras.getString("question"));
		langView.setText(extras.getString("language"));

		listView = (ListView) findViewById(R.id.answer_list);
		answerEditText = (EditText) findViewById(R.id.answer_question_bar);
		mAudioFooter = (AudioFragment) getFragmentManager().findFragmentById(R.id.audio_footer);
		
		answers = new AnswerList();
		boolean hasAnswers = extras.getBoolean("hasAnswers");
		boolean mHasRecording = extras.getBoolean("hasRecording");


		if (hasAnswers) {
			// add dummy answers 
			loadAnswersToUI();
		}
		if (mHasRecording) {
			mAnswerRecordingPath = extras.getString("recording");
		}

		mAudioFooter.activateRecordButton(true);
		mAudioFooter.activatePlayButton(false);
		setOnPostListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
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
					boolean answerHasRecording = mAudioFooter.hasRecording();
					String audioFilePath = mAudioFooter.getAudioFilePath();
					answers.addAnswer("Bearly a Group", answerText, answerHasRecording, audioFilePath);
					answerEditText.setText(""); 
					mAudioFooter.activateRecordButton(true);
					mAudioFooter.activatePlayButton(false);
					loadAnswersToUI();

					// remove re-record + replay buttons
					mAudioFooter.activateRecordButton(true);
					mAudioFooter.activatePlayButton(false);
					mAudioFooter.setHasRecording(false);
					// mAudioFooter.setRecordText(R.string.record);
				}
				return handled;
			}

		});
	}
}
