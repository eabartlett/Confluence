package com.example.confluence;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AskQuestionActivity extends BaseActivity {

    EditText questionEditText;
    Spinner languageSpinner;
    boolean hasRecording;
    private int VOICE_RECORDER_CODE = 1;
    String recording;
    
    
    private static final String LOG_TAG = "VoiceRecorderTest";
    private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + 
    		"/VoiceRecorderTest.3gp";
    private Button mRecordButton, mPlayButton, mAttachButton;
    private ImageView recordIcon, playIcon;
    private TextView recordButtonText, playButtonText;
    private LinearLayout mPlayLayout;
    private RelativeLayout mRecordLayout;
    //private SeekBar mSeekBar;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private boolean mStartPlaying = true, mStartRecording = true;
    private TextView mTimerText;
    public CountDownTimer mCountDownTimer = null;
    Menu menu;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);
		
        questionEditText = (EditText) findViewById(R.id.question_text);
        //questionEditText.setScroller(new Scroller(context)); 
        questionEditText.setMaxLines(3);
        questionEditText.setVerticalScrollBarEnabled(true); 
        questionEditText.setMovementMethod(new ScrollingMovementMethod());


        loadLanguages();
        
        mRecordButton = (Button) findViewById(R.id.record_button);
        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayLayout = (LinearLayout) findViewById(R.id.playback_footer);
        mRecordLayout = (RelativeLayout) findViewById(R.id.recording_footer);
        mTimerText = (TextView) findViewById(R.id.txt_timer);
        recordIcon = (ImageView) findViewById(R.id.record_icon);
        recordButtonText = (TextView) findViewById(R.id.button_text);
        playIcon = (ImageView) findViewById(R.id.play_icon);
        playButtonText = (TextView) findViewById(R.id.play_button_text);
        
        activateRecordButton(true);
    	activatePlayButton(false);
    	//activateAttachButton(false);

	}
	
	/**
	 * Gets user's languages and populates the filter by language spinner
	 * with these languages
	 * 
	 * Same as in NewsFeedActivity
	 */
	private void loadLanguages(){
		languageSpinner = (Spinner) findViewById(R.id.language_spinner);
		String[] langs = getUserLanguages();
		ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(
				this, R.layout.spinner_row, langs);
		languageSpinner.setAdapter(langAdapter);

	}

	/**
	 * Currently hard coded, would involve an API call in the final version
	 * @return - Returns array of the strings that are the languages a user uses
	 * 
	 * Same as in NewsFeedActivity
	 */
	private String[] getUserLanguages(){
		String[] langs = new String[5];

		langs[0] = "Select a language";
			//a different prompt than in newsfeed activity
		langs[1] = "English";
		langs[2] = "French";
		langs[3] = "German";
		langs[4] = "Spanish";
		
		return langs;
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ask_question, menu);
		return true;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        if (id == R.id.action_post) {
        	String questionText = questionEditText.getText().toString();
        	if (questionText != "") {
        		postQuestion(questionText);
        	} else {
        		//Toast
        	}
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void postQuestion(String questionText) {
        Intent postQuestionIntent = new Intent(AskQuestionActivity.this, PostedQuestionActivity.class);
        postQuestionIntent.putExtra("question", questionText);
        postQuestionIntent.putExtra("language", languageSpinner.getSelectedItem().toString());
        postQuestionIntent.putExtra("hasRecording", hasRecording);
        postQuestionIntent.putExtra("recording", recording);

        AskQuestionActivity.this.startActivity(postQuestionIntent);
        
        
		/*Intent postQuestionIntent = new Intent(AskQuestionActivity.this, AnswerActivity.class);
		postQuestionIntent.putExtra("question", questionText);
        postQuestionIntent.putExtra("language", languageSpinner.getSelectedItem().toString());
        postQuestionIntent.putExtra("hasAnswers", false); 
        	// ^ for interactive prototype
        postQuestionIntent.putExtra("hasRecording", hasRecording);
        postQuestionIntent.putExtra("recording", recording);
		AskQuestionActivity.this.startActivity(postQuestionIntent);*/


    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == VOICE_RECORDER_CODE) {
	        if (resultCode == RESULT_OK) {
	        	//Toast.makeText(this, "Audio attached", Toast.LENGTH_LONG).show();
	            // Check attached audio
	        	mPlayer = new MediaPlayer();
	            try {
	            	recording = data.getExtras().getString(Intent.EXTRA_TEXT);
		            hasRecording = true;
		            

	                mPlayer.setDataSource(data.getExtras().getString(Intent.EXTRA_TEXT));
	                mPlayer.prepare();
	                mPlayer.start();
	                
	            	new CountDownTimer(mPlayer.getDuration(), 1000) {
	            	     public void onTick(long millisUntilFinished) {
	            	    	 
	            	     }

	            	     public void onFinish() {
	            	    	 mPlayer.release();
	            	         mPlayer = null;

	            	     }
	            	}.start();
	            } catch (IOException e) {
	            	
	            }
	        }
	    }
	}
	
    /*public void addRecording(View v) {
        Intent voiceRecorderIntent = new Intent(AskQuestionActivity.this, VoiceRecorderActivity.class);
        AskQuestionActivity.this.startActivityForResult(voiceRecorderIntent, VOICE_RECORDER_CODE);
    }*/
    

    public void playCallback(View v) {
    	if (mStartPlaying){

    		playButtonText.setText(R.string.stop);
        	mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
                mPlayer.start();
                
                //mReRecordButton.setVisibility(View.VISIBLE);
                
                //mSeekBar.setVisibility(View.VISIBLE);
                //SeekBar.setMax(mPlayer.getDuration());
            	//activateRecordButton(false);
            	//activateReRecordButton(true);
            	//activateAttachButton(true);
                                
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
	    	//Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
	    	activatePlayButton(false);
	    	//activateAttachButton(false);
	    	//activateReRecordButton(false);
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
    
    
    public void attachCallback() {
    //public void attachCallback(View v) {
    	//Toast.makeText(this, "Audio attached!", Toast.LENGTH_LONG).show();
    	
    	Intent returnResultIntent = new Intent(Intent.ACTION_SEND);
    	Uri uri = Uri.parse(mFileName);
    	returnResultIntent.setType("audio/*");
    	returnResultIntent.putExtra(Intent.EXTRA_TEXT, mFileName);
    	setResult(Activity.RESULT_OK, returnResultIntent);
    	
    	/*will go back to previous activity*/

    	finish();
    }  
    
    public void removeCallback(View v) {
    	activatePlayButton(false);
    	recordButtonText.setText(R.string.add_recording);
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
    
}
