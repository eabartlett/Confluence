package com.example.confluence.answers;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.confluence.R;

public class AudioFragment extends Fragment {

	private static final String LOG_TAG = "RecordView";

	private Button mReRecordButton, mPlayButton, mRecordButton;
	private LinearLayout mPlayLayout, mReRecordLayout;
	private RelativeLayout mRecordLayout;
	private TextView mTimerText, recordButtonText, playButtonText;
	private ImageView recordIcon, playIcon;
	private MediaPlayer mPlayer = null;
	
	OnTimerStarted mCallback;
	public CountDownTimer mCountDownTimer = null;

	
	private String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.3gp";

	private boolean mHasRecording=false, mStartPlaying = true, mStartRecording=true;
	private MediaRecorder mRecorder = null;
	private String recording;

	
	// FRAGMENT LIFE-CYCLE METHODS
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.record_view, container, false);
		mRecordButton = (Button) v.findViewById(R.id.record_button);
		mReRecordButton = (Button) v.findViewById(R.id.rerecord_button);
		mPlayButton = (Button) v.findViewById(R.id.play_button);
		mPlayLayout = (LinearLayout) v.findViewById(R.id.playback_footer);
		mRecordLayout = (RelativeLayout) v.findViewById(R.id.recording_footer);
		mReRecordLayout = (LinearLayout) v.findViewById(R.id.rerecord_footer);
		mTimerText = (TextView) v.findViewById(R.id.txt_timer);
		recordIcon = (ImageView) v.findViewById(R.id.record_icon);
		recordButtonText = (TextView) v.findViewById(R.id.button_text);
		playIcon = (ImageView) v.findViewById(R.id.play_icon);
		playButtonText = (TextView) v.findViewById(R.id.play_button_text);

		setRecordButtonListener();
		setReRecordButtonListener();
		setPlayButtonListener();
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnTimerStarted) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}
	
	@Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

	// GETTER + SETTER METHODS
	
	public boolean setRecordText(int resId) {
		mRecordButton.setText(resId);
		return true;
	}

	public boolean setRecordText(String text) {
		mRecordButton.setText(text);
		return true;
	}

	public boolean hasRecording() {
		return mHasRecording;
	}

	public boolean setHasRecording(boolean hasRecording) {
		mHasRecording = hasRecording;
		return true;
	}

	public String getAudioFilePath() {
		return mFileName;
	}

	// TIMER INTERFACE METHODS
	public interface OnTimerStarted {
		void setCountdownText(String string);
	}


	// AUDIO HANDLING METHODS

	/**
	 * Sets onClickListener for record button to start recording
	 */
	private void setRecordButtonListener() { 
		mRecordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recordCallback(v);
			}
		});
	}

	/**
	 * Sets onClickListener for record button to start recording
	 */
	private void setReRecordButtonListener() { 
		mReRecordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recordCallback(v);
			}
		});
	}


	/**
	 * Sets onClickListener for play button to playback audio.
	 */
	private void setPlayButtonListener() { 
		mPlayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playCallback(v);
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
	
	public void activateReRecordButton(boolean bActivate) {
		mReRecordButton.setClickable(bActivate); 
		if (bActivate) {
			mReRecordLayout.setVisibility(View.VISIBLE);
		}
		else {
			mReRecordLayout.setVisibility(View.GONE);
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
						// recordButtonText.setText("Re-record");
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
				e.printStackTrace();
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
					mCallback.setCountdownText(Integer.toString((int) (millisUntilFinished/1000)));
				}

				public void onFinish() {
					mCallback.setCountdownText("");
					stopRecording();
					//attachCallback();

					recordButtonText.setText(R.string.record);
					// recordIcon.setImageResource(R.drawable.ic_action_mic_active);
					activateReRecordButton(true);
					activatePlayButton(true);
					mStartRecording = true;
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
		mHasRecording = true;
	}
}
