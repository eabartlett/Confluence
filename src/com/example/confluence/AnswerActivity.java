package com.example.confluence;


import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.confluence.OpenAnswerActivity.GetAudioInAnswer;
import com.example.confluence.answers.AnswerArrayAdapter;
import com.example.confluence.answers.AudioFragment;
import com.example.confluence.dbtypes.Answer;
import com.example.confluence.newsfeed.AnswerView;

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
	
	private String mQuestionId, mAnswerId, mAnswerIdPosted, mAnswerAudioPath;
	ConfluenceAPI mApi;
	private String mFileName;
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
		mFileName = mAudioFooter.getAudioFilePath();
		
		
		mAnswers = new ArrayList<Answer>();
		
		mListView.setAdapter(new AnswerArrayAdapter(this,
				R.layout.activity_answer,
				mAnswers)); 
		
		mListView.setFocusable(true);
		mListView.setFocusableInTouchMode(true);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d("Confluence Listview", "Item clicked");
				ViewGroup viewGroup = (ViewGroup) arg1;
				AnswerView answerView = (AnswerView) viewGroup.getChildAt(0);
				Answer a = answerView.getAnswer();
				Intent qIntent = new Intent(AnswerActivity.this, OpenAnswerActivity.class);

				qIntent.putExtra("id", a.getAnswerId());
				qIntent.putExtra("question", a.getText());
				qIntent.putExtra("language", a.getLanguage());
				startActivity(qIntent);
			}
			
		});
		
		

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
	
	private void postAnswer(String answerText) {
		
		// boolean answerHasRecording = mAudioFooter.hasRecording();
		Answer newAnswer = new Answer(
				"nahush", 
				NewsFeedActivity.mUser.getId(), 
				answerText, 
				mAudioFooter.getAudioFilePath(), 
				mQuestionId,
				0 );
		
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
				try {
					mAnswerId = jAns.getString("_id");
					if (mApi.postAnswerAudio(mFileName, mAnswerId) != null)
						return true;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// return true;
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
	
	//pulled from OpenAnswerActivity
	public void playAudioInAnswer(String answerId, String audioPath) {
		new GetAudioInAnswer().execute("");
		mAnswerIdPosted = answerId;
		mAnswerAudioPath = audioPath;
		
	}
	
	private class GetAudioInAnswer extends AsyncTask<String, Integer, Boolean>{

		// @Override
		protected Boolean doInBackground(String... params) {
			if (mApi.getAudio(mAnswerIdPosted, mFileName, "answer")) {			
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
