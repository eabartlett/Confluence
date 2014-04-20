package com.example.confluence;

import java.io.IOException;

import android.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
//import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class VoiceRecorderActivity extends BaseActivity
{
    private static final String LOG_TAG = "VoiceRecorderTest";
    private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + 
    		"/VoiceRecorderTest.3gp";
    private Button mRecordButton, mPlayButton, mAcceptButton, mReRecordButton;
    private ImageView recordIcon;
    //private SeekBar mSeekBar;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private boolean mStartPlaying = true, mStartRecording = true;
    private TextView mTimerText;
    public CountDownTimer mCountDownTimer = null;
    Menu menu;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voice_recorder);
        
        mRecordButton = (Button) findViewById(R.id.bt_record);
        mPlayButton = (Button) findViewById(R.id.bt_play);
        mReRecordButton = (Button) findViewById(R.id.bt_rerecord);
        mAcceptButton = (Button) findViewById(R.id.bt_accept);
        recordIcon = (ImageView) findViewById(R.id.record_icon);

        mPlayButton.setClickable(false);
        
        mAcceptButton.setClickable(false);

        mTimerText = (TextView) findViewById(R.id.txt_timer);
        //mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        //mSeekBar.setVisibility(View.INVISIBLE);
        
        
        
        activateRecordButton(true);
    	activatePlayButton(false);
    	activateAcceptButton(false);
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
    

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.voice_recorder, menu);
    this.menu = menu;

    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    return true;

}

    public void recordCallback(View v) {
    	if (mStartRecording) {
	    	startRecording();
	    	mRecordButton.setText(R.string.stop);
	    	recordIcon.setImageResource(R.drawable.ic_action_mic_muted);
	    	Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
	    	activatePlayButton(false);
	    	activateAcceptButton(false);
	    	activateReRecordButton(false);
	    	mStartRecording = false;
	    	mCountDownTimer = new CountDownTimer(10000, 500) {
	    	     public void onTick(long millisUntilFinished) {
	    	    	 mTimerText.setText(Integer.toString((int) (millisUntilFinished / 1000)));
	    	     }
	
	    	     public void onFinish() {
	    	    	 mTimerText.setText("");
	    	    	 stopRecording();
	    	    	 mRecordButton.setText(R.string.add_recording);
	    		     recordIcon.setImageResource(R.drawable.ic_action_mic_active);
	    	     	 activatePlayButton(true);
	    	     	 activateAcceptButton(true);
	    	     	 activateReRecordButton(true);
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
                
                mReRecordButton.setVisibility(View.VISIBLE);
                
                //mSeekBar.setVisibility(View.VISIBLE);
                //SeekBar.setMax(mPlayer.getDuration());
            	activateRecordButton(false);
            	activateReRecordButton(true);
            	activateAcceptButton(true);
            	mStartPlaying = false;
            	mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_stop, 0, 0);
            	//Toast.makeText(this, "Playing..", Toast.LENGTH_SHORT).show();
            	mCountDownTimer = new CountDownTimer(mPlayer.getDuration(), 1000) {
            	     public void onTick(long millisUntilFinished) {
            	    	 //mSeekBar.setProgress(mPlayer.getCurrentPosition());
            	     }

            	     public void onFinish() {
            	    	 mPlayer.release();
            	         mPlayer = null;
            	    	 mPlayButton.setText(R.string.play);
            	    	 mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play_active, 0, 0);
            	         activateRecordButton(true);
            	         activateReRecordButton(true);
            	         activateAcceptButton(true);
            	         //mSeekBar.setVisibility(View.INVISIBLE);
            	         mRecordButton.setVisibility(View.GONE);
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
    	Intent returnResultIntent = new Intent(Intent.ACTION_SEND);
    	Uri uri = Uri.parse(mFileName);
    	returnResultIntent.setType("audio/*");
    	returnResultIntent.putExtra(Intent.EXTRA_TEXT, mFileName);
    	setResult(Activity.RESULT_OK, returnResultIntent);
    	finish();
    }    
    
    public void rerecord(View v) {
    	activateRecordButton(true);
    	activatePlayButton(false);
    	activateAcceptButton(false);
    	activateReRecordButton(false);
    }
    
    public void activateRecordButton(boolean bActivate) {
    	mRecordButton.setClickable(bActivate);
    	if (bActivate) {
	    	recordIcon.setImageResource(R.drawable.ic_action_mic_active);
    		mRecordButton.setVisibility(View.VISIBLE);
    	}
    	else {
    		mRecordButton.setVisibility(View.GONE);
    	}
    }
    
    public void activateReRecordButton(boolean bActivate) {
    	mReRecordButton.setClickable(bActivate);
    	if (bActivate) {
	    	recordIcon.setImageResource(R.drawable.ic_action_mic_active);
    		mReRecordButton.setVisibility(View.VISIBLE);
    	}
    	else {
    		mReRecordButton.setVisibility(View.GONE);
    	}
    }    
    
    public void activatePlayButton(boolean bActivate) {
    	mPlayButton.setClickable(bActivate);
    	if (bActivate) {
    		mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play_active, 0, 0);
        	mPlayButton.setVisibility(View.VISIBLE);

    	}
    	else {
        	mPlayButton.setVisibility(View.GONE);
    	}
    }
    
    public void activateAcceptButton(boolean bActivate) {
        mAcceptButton.setClickable(bActivate);
    	if (bActivate) {
    		mAcceptButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_accept_active, 0, 0, 0);
   	 		mAcceptButton.setVisibility(View.VISIBLE);
    	}
    	else {
    		//mAcceptButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_accept_inactive, 0, 0);
   	 		mAcceptButton.setVisibility(View.GONE);
    	}
    }
}