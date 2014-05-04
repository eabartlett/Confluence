package com.example.confluence;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PostedQuestionActivity extends BaseActivity {

    String question;
    String language;
    String type;
    boolean hasRecording;
    String recording;
    TextView questionTextView;
    TextView categories;
    private MediaPlayer mPlayer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posted_question);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            question = extras.getString("question");
            language = extras.getString("language");
            hasRecording = extras.getBoolean("hasRecording");
            recording = extras.getString("recording");
        }

        /*categories = (TextView) findViewById(R.id.categories);

        categories.setText("asked about " + language);*/

        questionTextView = (TextView) findViewById(R.id.question);
        questionTextView.setText(question + "\n" + language);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.posted_question, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void playCallback(View v) {

            // Check attached audio
        	mPlayer = new MediaPlayer();
            try {           
                mPlayer.setDataSource(recording);
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
