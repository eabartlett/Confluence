package com.example.confluence;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.confluence.answers.AnswerArrayAdapter;
import com.example.confluence.answers.AudioFragment;
import com.example.confluence.dbtypes.Answer;

/**
 * AnswerActiv
 * ity handles all answers associated with a question. 
 * @author brian
 *
 */
public class AnswerActivity extends BaseActivity {

	private ListView mListView;
	private EditText mAnswerEditText;
	private AudioFragment mAudioFooter;

	private ArrayList<Answer> mAnswers;
	
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

		mListView = (ListView) findViewById(R.id.answer_list);
		mAnswerEditText = (EditText) findViewById(R.id.answer_question_bar);
		mAudioFooter = (AudioFragment) getFragmentManager().findFragmentById(R.id.audio_footer);		
		mAnswers = new ArrayList<Answer>();
		
		mListView.setAdapter(new AnswerArrayAdapter(getApplicationContext(),
				R.layout.activity_answer,
				mAnswers)); 
		
		boolean hasAnswers = extras.getBoolean("hasAnswers");
		boolean mHasRecording = extras.getBoolean("hasRecording");


		if (hasAnswers) {
			// add dummy answers 
			loadAnswersToUI();
		}
		if (mHasRecording) {
			extras.getString("recording");
		}

		mAudioFooter.activateRecordButton(true);
		mAudioFooter.activatePlayButton(false);
		// setOnPostListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
        	final String questionText = mAnswerEditText.getText().toString();
        	boolean isTextEmpty = questionText.trim().isEmpty();
        	boolean hasRecording = mAudioFooter.hasRecording();
        	if (isTextEmpty && !hasRecording) {
        		Toast toast = Toast.makeText(getApplicationContext(), "Please add text and a recording.", Toast.LENGTH_SHORT);
    			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
    			toast.show();
        	} else if (isTextEmpty) {
        		Toast toast = Toast.makeText(getApplicationContext(), "Please add text.", Toast.LENGTH_SHORT);
    			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
    			toast.show();
        	} else if (!hasRecording) {
        		// Display warning message
        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        		builder.setMessage(R.string.no_audio_alert)
        			.setPositiveButton("Yes", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							postAnswer(questionText);
						}
        			}).setNegativeButton("No", null).show();
        	} else {
        		postAnswer(questionText);
        	}
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void postAnswer(String answerText) {

		boolean answerHasRecording = mAudioFooter.hasRecording();
		String audioFilePath = mAudioFooter.getAudioFilePath();
		Answer newAnswer = new Answer("03", "Bearly a Group", answerText, audioFilePath, null);
		mAnswers.add(newAnswer);
		mAnswerEditText.setText(""); 
		mAudioFooter.activateRecordButton(true);
		mAudioFooter.activatePlayButton(false);
		loadAnswersToUI();

		// Reset audioFooter UI
		mAudioFooter.activateRecordButton(true);
		mAudioFooter.activatePlayButton(false);
		mAudioFooter.setHasRecording(false);
	}


	/**
	 * Loads answers contained in AnswerList answers to the ListView UI.
	 */
	private void loadAnswersToUI() {
		AnswerArrayAdapter answerAdapter = (AnswerArrayAdapter) mListView.getAdapter();
		answerAdapter.notifyDataSetChanged();
		/*AnswerArrayAdapter answerAdapter = 
				new AnswerArrayAdapter(getApplicationContext(),
						R.layout.activity_answer, 
						mAnswers.getAnswers());
		mListView.setAdapter(answerAdapter);*/
	}
}
