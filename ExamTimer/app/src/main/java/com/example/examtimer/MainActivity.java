package com.example.examtimer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

class InputFilterMinMax implements InputFilter {

	private int min, max;

	public InputFilterMinMax(int min, int max) {
		this.min = min;
		this.max = max;
	}

	InputFilterMinMax(String min, String max) {
		this.min = Integer.parseInt(min);
		this.max = Integer.parseInt(max);
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		try {
			int input = Integer.parseInt(dest.toString() + source.toString());
			if (isInRange(min, max, input))
				return null;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return "";
	}

	private boolean isInRange(int a, int b, int c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}
}


public class MainActivity extends AppCompatActivity implements LifecycleObserver {


	boolean flag = false;
	NotificationManagerCompat notificationManager;
	NotificationCompat.Builder builder;

	long currHour,currMin,currSec;

	long hour,min,sec;
	EditText hours,minutes,seconds;
	Button startButton,pause,reset;
	MediaPlayer m1;
	CountDownTimer timer;
	long time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		hours = findViewById(R.id.hours);
		minutes = findViewById(R.id.minutes);
		seconds = findViewById(R.id.seconds);

		minutes.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
		seconds.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

		startButton = findViewById(R.id.button);
		pause = findViewById(R.id.pause);
		reset = findViewById(R.id.reset);

		pause.setEnabled(false);

		m1 = MediaPlayer.create(this,R.raw.audio);

		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleButton(false);



				String hh = hours.getText().toString();
				String mm = minutes.getText().toString();
				String ss = seconds.getText().toString();

				if(!TextUtils.isEmpty(hh)){
					hour = Long.parseLong(hh);
				}
				if(!TextUtils.isEmpty(mm)){
					min = Long.parseLong(mm);
				}
				if(!TextUtils.isEmpty(ss)){
					sec = Long.parseLong(ss);
				}



				time = (hour*3600 + min*60 + sec)*1000;

				timer = new CountDownTimer(time, 1000) {

					public void onTick(long millisUntilFinished) {

						currSec = millisUntilFinished / 1000;
						currHour = currSec/3600;
						currSec %= 3600;

						currMin = currSec/60;
						currSec%=60;


						hours.setText(String.valueOf(currHour));
						minutes.setText(String.valueOf(currMin));
						seconds.setText(String.valueOf(currSec));
						updateNotifications();

					}

					@NonNull
					@Override
					public String toString() {
						return currHour+":"+currMin+":"+currSec;
					}

					public void onFinish() {
						startButton.setText("START");
						Toast.makeText(MainActivity.this,"Time Up!!",Toast.LENGTH_SHORT).show();
						m1.start();
						toggleButton(true);
					}
				};

				timer.start();
			}
		});

		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.cancel();
				toggleButton(true);

				startButton.setText("RESUME");
			}
		});

		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.cancel();
				startButton.setText("START");
				hours.setText(String.valueOf(hour));
				minutes.setText(String.valueOf(min));
				seconds.setText(String.valueOf(sec));

			}
		});

	}

	void toggleButton(boolean toggle){
		hours.setEnabled(toggle);
		minutes.setEnabled(toggle);
		seconds.setEnabled(toggle);
		startButton.setEnabled(toggle);
		pause.setEnabled(!toggle);
		reset.setEnabled(toggle);

		if(toggle) {
			hours.setTextColor(Color.BLACK);
			minutes.setTextColor(Color.BLACK);
			seconds.setTextColor(Color.BLACK);
		}else{
			hours.setTextColor(Color.DKGRAY);
			minutes.setTextColor(Color.DKGRAY);
			seconds.setTextColor(Color.DKGRAY);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onPause() {
		super.onPause();

		flag = true;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "Notif1";
			String description = "Notif Description";
			int importance = NotificationManager.IMPORTANCE_LOW;
			NotificationChannel channel = new NotificationChannel("MyChannel", name, importance);
			channel.setDescription(description);
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			assert notificationManager != null;
			notificationManager.createNotificationChannel(channel);
		}



		builder = new NotificationCompat.Builder(this, "MyChannel")
				.setSmallIcon(R.drawable.ic_baseline_timer_24)
				.setContentTitle("Timer:")
				.setContentText(timer.toString())
				.setPriority(NotificationCompat.PRIORITY_LOW);

		notificationManager = NotificationManagerCompat.from(this);

		notificationManager.notify(100, builder.build());

		Log.d("BackOrFore","In BackGround");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(flag){

			notificationManager.cancel(100);
			flag = false;
		}

	}

	public void updateNotifications(){

		if(flag){
			builder.setContentText(timer.toString());
			notificationManager.notify(100,builder.build());
		}
	}


}

