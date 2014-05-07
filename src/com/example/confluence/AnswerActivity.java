package com.example.confluence;


import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
	
	private String mQuestionId;
	ConfluenceAPI mApi;
	private String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.3gp";
	private Button playButton;
	private MediaPlayer mPlayer;
	private CountDownTimer mCountDownTimer; 
	
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
		mQuestionId = extras.getString("id");

		mListView = (ListView) findViewById(R.id.answer_list);
		mAnswerEditText = (EditText) findViewById(R.id.answer_question_bar);
		mAudioFooter = (AudioFragment) getFragmentManager().findFragmentById(R.id.audio_footer);		
		mAnswers = new ArrayList<Answer>();
		
		mListView.setAdapter(new AnswerArrayAdapter(getApplicationContext(),
				R.layout.activity_answer,
				mAnswers)); 
		
		mApi = new ConfluenceAPI();
		playButton = (Button) findViewById (R.id.bt_play);

		new callGetAnswersByQuestion().execute("");

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

	public void playAudioInQuestion(View view) {
		new GetAudioInQuestion().execute("");
		
	}
	
	private class GetAudioInQuestion extends AsyncTask<String, Integer, Boolean>{

		// @Override
		protected Boolean doInBackground(String... params) {
			if (mApi.getAudio(mQuestionId, mFileName, "question")) {			
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
				} catch (IllegalArgumentException | SecurityException
						| IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
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
	
	private void postAnswer(String answerText) {
		
		// boolean answerHasRecording = mAudioFooter.hasRecording();
		Answer newAnswer = new Answer(
				"", 
				NewsFeedActivity.mUser.getId(), 
				answerText, 
				mAudioFooter.getAudioFilePath(), 
				mQuestionId);
		
		new callPostAnswer().execute(newAnswer);

		// Reset audioFooter UI
		mAudioFooter.activateRecordButton(true);
		mAudioFooter.activatePlayButton(false);
		mAudioFooter.setHasRecording(false);
		
		mAnswerEditText.setText(""); 
	}

	private class callPostAnswer extends AsyncTask<Answer, Integer, Boolean>{

		// @Override
		protected Boolean doInBackground(Answer... answer) {
			JSONObject jAns = mApi.postAnswer(answer[0]);
			if (jAns != null) {			
				return true;
			}
			Log.d("Confluence ****", "PostAnswer failed");
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				new callGetAnswersByQuestion().execute("");		
			}
	    }
	}

	private class callGetAnswersByQuestion extends AsyncTask<String, Integer, Answer[]>{

		// @Override
		protected Answer[] doInBackground(String... param) {
			Answer[] answers;
			answers = mApi.getAnswersByQuestion(mQuestionId);
			if (answers == null) {
				Log.d("Confluence ****", "Danger");
			}
			return answers;
		}
		
		@Override
		protected void onPostExecute(Answer[] answers) {
			if (answers != null) 
			{
				loadAnswersToUI(answers);
			}
	    }
	}
	
	/**
	 * Loads answers contained in AnswerList answers to the ListView UI.
	 */
	private void loadAnswersToUI(Answer[] answers) {
		AnswerArrayAdapter answerAdapter = (AnswerArrayAdapter) mListView.getAdapter();
		answerAdapter.clear();
		answerAdapter.addAll(answers);
		answerAdapter.notifyDataSetChanged();
	}
}
