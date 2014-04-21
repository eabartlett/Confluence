package com.example.confluence;


import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.confluence.answers.AnswerArrayAdapter;
import com.example.confluence.answers.AnswerList;

/**
 * AnswerActivity handles all answers associated with a question. 
 * @author brian
 *
 */
public class AnswerActivity extends BaseActivity {

	private static final String LOG_TAG = "AnswerVoiceRecorderTest";

	private ListView listView;
	private EditText answerEditText;
	private Button mRecordbutton, mPlayButton, mRecordButton;
	private LinearLayout mPlayLayout;
	private RelativeLayout mRecordLayout;
	private TextView mTimerText, recordButtonText, playButtonText;
	private ImageView recordIcon, playIcon;

	private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + 
			"/VoiceRecorderTest.3gp";
	private AnswerList answers;
	private boolean hasAnswers, mHasRecording, mStartPlaying = true, mStartRecording=true;
	private MediaRecorder mRecorder = null;
	private String recording;
	private int VOICE_RECORDER_CODE = 1;
	private MediaPlayer mPlayer = null;
	public CountDownTimer mCountDownTimer = null;

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

		mRecordButton = (Button) findViewById(R.id.record_button);
		mPlayButton = (Button) findViewById(R.id.play_button);
		mPlayLayout = (LinearLayout) findViewById(R.id.playback_footer);
		mRecordLayout = (RelativeLayout) findViewById(R.id.recording_footer);
		mTimerText = (TextView) findViewById(R.id.txt_timer);
		recordIcon = (ImageView) findViewById(R.id.record_icon);
		recordButtonText = (TextView) findViewById(R.id.button_text);
		playIcon = (ImageView) findViewById(R.id.play_icon);
		playButtonText = (TextView) findViewById(R.id.play_button_text);

		listView = (ListView) findViewById(R.id.answer_list);
		answerEditText = (EditText) findViewById(R.id.answer_question_bar);

		answers = new AnswerList();

		hasAnswers = extras.getBoolean("hasAnswers");
		mHasRecording = extras.getBoolean("hasRecording");


		if (hasAnswers) {
			// add dummy answers 
			loadAnswersToUI();
		}
		if (mHasRecording) {
			recording = extras.getString("recording");
		}

		activateRecordButton(true);
		activatePlayButton(false);
		setOnPostListener();
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
					answers.addAnswer("Bearly a Group", answerText, mHasRecording, mFileName);
					answerEditText.setText(""); 
					activateRecordButton(true);
					activatePlayButton(false);
					loadAnswersToUI();

					// remove re-record + replay buttons
					activateRecordButton(true);
					activatePlayButton(false);
					recordButtonText.setText(R.string.record);
					mHasRecording = false;
				}
				return handled;
			}

		});
	}

	public void activateRecordButton(boolean bActivate) {
		mRecordButton.setClickable(bActivate);
		if (bActivate) {
			recordIcon.setImageResource(R.drawable.ic_action_mic_active);
			mRecordLayout.setVisibility(View.VISIBLE);
		}
		else {
			mRecordLayout.setVisibility(View.GONE);
		}
	}


	public void activatePlayButton(boolean bActivate) {
		mPlayButton.setClickable(bActivate);
		if (bActivate) {
			//playIcon.setImageResource(R.drawable.ic_action_play_active);;
			mPlayLayout.setVisibility(View.VISIBLE);
		}
		else {
			mPlayLayout.setVisibility(View.GONE);
		}
	}

	public void playCallback(View v) {
		if (mStartPlaying){

			playButtonText.setText(R.string.stop);
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(mFileName);
				mPlayer.prepare();
				mPlayer.start();

				mStartPlaying = false;
				playIcon.setImageResource(R.drawable.ic_action_stop);
				//Toast.makeText(this, "Playing..", Toast.LENGTH_SHORT).show();
				mCountDownTimer = new CountDownTimer(mPlayer.getDuration(), 1000) {
					public void onTick(long millisUntilFinished) {
						//mSeekBar.setProgress(mPlayer.getCurrentPosition());
					}

					public void onFinish() {
						mPlayer.release();
						mPlayer = null;
						recordButtonText.setText("Re-record");
						playButtonText.setText(R.string.play);
						playIcon.setImageResource(R.drawable.ic_action_play_active);

						//activateRecordButton(true);
						//activateReRecordButton(true);
						//activateAttachButton(true);
						//mSeekBar.setVisibility(View.INVISIBLE);
						//mRecordButton.setVisibility(View.GONE);
						mStartPlaying = true;
					}
				}.start();
			} catch (IOException e) {
				Log.e(LOG_TAG, "prepare() failed");
			}
		}
		else
		{
			if (mCountDownTimer != null){
				mCountDownTimer.cancel();
				mCountDownTimer.onFinish();
			}
		}    	
	}

	public void recordCallback(View v) {
		if (mStartRecording) {
			startRecording();
			recordButtonText.setText(R.string.stop);
			recordIcon.setImageResource(R.drawable.ic_action_mic_muted);
			activatePlayButton(false);
			mStartRecording = false;
			mCountDownTimer = new CountDownTimer(10000, 500) {
				public void onTick(long millisUntilFinished) {
					mTimerText.setText(Integer.toString((int) (millisUntilFinished / 1000)));
				}

				public void onFinish() {
					mTimerText.setText("");
					stopRecording();
					//attachCallback();

					recordButtonText.setText("Re-record");
					recordIcon.setImageResource(R.drawable.ic_action_mic_active);
					activatePlayButton(true);
					//activateAttachButton(true);
					//activateReRecordButton(true);
					mStartRecording = true;
					mHasRecording = true;
				}
			}.start();
		}
		else {
			if (mCountDownTimer != null){
				mCountDownTimer.cancel();
				mCountDownTimer.onFinish();
			}
		}
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		recording = mFileName;
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}
	
	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;

	}
}
