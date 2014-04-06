package com.example.voicerecorder;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class VoiceRecorderActivity extends Activity
{
    private static final String LOG_TAG = "VoiceRecorderTest";
    private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + 
    		"/VoiceRecorderTest.3gp";
    private Button mRecordButton, mPlayButton, mAcceptButton;
    private SeekBar mSeekBar;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private boolean mStartPlaying = true, mStartRecording = true;
    private TextView mTimerText;
    public CountDownTimer mCountDownTimer = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voice_recorder);
        
        mRecordButton = (Button) findViewById(R.id.bt_record);
        mPlayButton = (Button) findViewById(R.id.bt_play);
        mPlayButton.setClickable(false);
        mAcceptButton = (Button) findViewById(R.id.bt_accept);
        mAcceptButton.setClickable(false);
        mTimerText = (TextView) findViewById(R.id.txt_timer);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mSeekBar.setVisibility(View.INVISIBLE);
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
    
    public void recordCallback(View v) {
    	if (mStartRecording) {
	    	startRecording();
	    	mRecordButton.setText(R.string.stop);
	    	mRecordButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_mic_muted, 0, 0);
	    	Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
	    	activatePlayButton(false);
	    	activateAcceptButton(false);
	    	mStartRecording = false;
	    	mCountDownTimer = new CountDownTimer(10000, 500) {
	    	     public void onTick(long millisUntilFinished) {
	    	    	 mTimerText.setText(Integer.toString((int) (millisUntilFinished / 1000)));
	    	     }
	
	    	     public void onFinish() {
	    	    	 mTimerText.setText("");
	    	    	 stopRecording();
	    	    	 mRecordButton.setText(R.string.start_recording);
	    	    	 mRecordButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_mic_active, 0, 0);
	    	     	 activatePlayButton(true);
	    	     	 activateAcceptButton(true);
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
    
    public void playCallback(View v) {
    	if (mStartPlaying){
    		mPlayButton.setText(R.string.stop);
        	mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
                mPlayer.start();
                
                mSeekBar.setVisibility(View.VISIBLE);
                mSeekBar.setMax(mPlayer.getDuration());
            	activateRecordButton(false);
            	activateAcceptButton(false);
            	mStartPlaying = false;
            	mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_stop, 0, 0);
            	Toast.makeText(this, "Playing..", Toast.LENGTH_SHORT).show();
            	mCountDownTimer = new CountDownTimer(mPlayer.getDuration(), 1000) {
            	     public void onTick(long millisUntilFinished) {
            	    	 mSeekBar.setProgress(mPlayer.getCurrentPosition());
            	     }

            	     public void onFinish() {
            	    	 mPlayer.release();
            	         mPlayer = null;
            	    	 mPlayButton.setText(R.string.play);
            	    	 mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play_active, 0, 0);
            	         activateRecordButton(true);
            	         activateAcceptButton(true);
            	         mSeekBar.setVisibility(View.INVISIBLE);
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

    public void acceptCallback(View v) {
    	Toast.makeText(this, "Audio attached!", Toast.LENGTH_LONG).show();
    	mRecordButton.setText(R.string.start_recording);
    	activateRecordButton(true);
    	activatePlayButton(false);
    	activateAcceptButton(false);
    }    
    
    public void activateRecordButton(boolean bActivate) {
    	mRecordButton.setClickable(bActivate);
    	if (bActivate) {
    		mRecordButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_mic_active, 0, 0);
    		mRecordButton.setTextColor(Color.parseColor("#ffffff"));
    	}
    	else {
    		mRecordButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_mic_inactive, 0, 0);
    		mRecordButton.setTextColor(Color.parseColor("#333333"));
    	}
    }
    
    public void activatePlayButton(boolean bActivate) {
    	mPlayButton.setClickable(bActivate);
    	if (bActivate) {
    		mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play_active, 0, 0);
   	 		mPlayButton.setTextColor(Color.parseColor("#ffffff")); 
    	}
    	else {
    		mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play_inactive, 0, 0);
   	 		mPlayButton.setTextColor(Color.parseColor("#333333"));  
    	}
    }
    
    public void activateAcceptButton(boolean bActivate) {
    	mAcceptButton.setClickable(bActivate);
    	if (bActivate) {
    		mAcceptButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_accept_active, 0, 0);
   	 		mAcceptButton.setTextColor(Color.parseColor("#ffffff"));
    	}
    	else {
    		mAcceptButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_accept_inactive, 0, 0);
   	 		mAcceptButton.setTextColor(Color.parseColor("#333333"));
    	}
    }
}