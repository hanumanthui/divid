package com.eminosoft.activity;

import com.eminosoft.divid.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends Activity {

	private TextView timerValue;
	private long startTime = 0L;
	private Handler customHandler = new Handler();

	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_activity);


		startTime = SystemClock.uptimeMillis();
		customHandler.postDelayed(updateTimerThread, 0);



		timerValue = (TextView) findViewById(R.id.timerValue);

		/*startButton = (Button) findViewById(R.id.startButton);

		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

		pauseButton = (Button) findViewById(R.id.pauseButton);

		pauseButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);

			}
		});*/

	}
	
	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 100);
			timerValue.setText("" + mins + ":"+ String.format("%02d", secs) + ":"+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 1*1000);
		}
	};
}