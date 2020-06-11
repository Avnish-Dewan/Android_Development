package com.example.timerapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	MediaPlayer mediaPlayer;

	boolean flagNotifications = false;
	NotificationManagerCompat notificationManager;
	NotificationCompat.Builder builder;

	boolean flag = false;

	int currHour,currMin,currSec;

	NumberPicker hours,minutes,seconds;
	final String TAG = "NUMBERPICKER";

	CountDownTimer timer;

	Button start,pause,reset;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		hours = findViewById(R.id.hours);
		minutes =findViewById(R.id.minutes);
		seconds = findViewById(R.id.seconds);



		start = findViewById(R.id.start);
		pause = findViewById(R.id.pause);
		reset = findViewById(R.id.reset);

		mediaPlayer = MediaPlayer.create(this,R.raw.complete);

		setLimits();

		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				long time = (hours.getValue()*3600 + minutes.getValue()*60 + seconds.getValue())*1000;

				toggleButton(false);

				timer = new CountDownTimer(time,1000) {
					@Override
					public void onTick(long millisUntilFinished) {

						currSec = (int)millisUntilFinished / 1000;
						currHour = currSec/3600;
						currSec %= 3600;

						currMin = currSec/60;
						currSec%=60;

						hours.setValue(currHour);
						minutes.setValue(currMin);
						seconds.setValue(currSec);

						updateNotifications();
					}
					@NonNull
					@Override
					public String toString() {

						return currHour+":"+currMin+":"+currSec;
					}


					@Override
					public void onFinish() {
						mediaPlayer.start();

						Toast.makeText(MainActivity.this,"Time Up!!!",Toast.LENGTH_SHORT).show();

						toggleButton(true);
					}
				};

				timer.start();
			}
		});


		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!flag){
					pause.setText("Resume");
					timer.cancel();
					togglePickers(true);
					flag = true;
				}else{
					pause.setText("Pause");
					togglePickers(false);
					start.callOnClick();
					flag = false;
				}
			}
		});

		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.cancel();
				toggleButton(true);
				hours.setValue(0);
				minutes.setValue(0);
				seconds.setValue(0);
			}
		});

	}

	private void togglePickers(boolean enabled){
		hours.setEnabled(enabled);
		minutes.setEnabled(enabled);
		seconds.setEnabled(enabled);
	}

	public void toggleButton(boolean enabled){

		togglePickers(enabled);

		start.setEnabled(enabled);
		pause.setEnabled(!enabled);
		reset.setEnabled(!enabled);

		if(!enabled) {
			pause.setVisibility(View.VISIBLE);
			reset.setVisibility(View.VISIBLE);
			start.setVisibility(View.INVISIBLE);
		}else{
			pause.setVisibility(View.INVISIBLE);
			reset.setVisibility(View.INVISIBLE);
			start.setVisibility(View.VISIBLE);
		}

	}

	private void setLimits() {

		hours.setMaxValue(99);
		hours.setMinValue(0);
		minutes.setMaxValue(59);
		minutes.setMinValue(0);
		seconds.setMaxValue(59);
		seconds.setMinValue(0);
	}



	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onPause() {
		super.onPause();

		flagNotifications = true;

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
		if(flagNotifications){

			notificationManager.cancel(100);
			flagNotifications = false;
		}

	}

	public void updateNotifications(){

		if(flagNotifications){
			builder.setContentText(timer.toString());
			notificationManager.notify(100,builder.build());
		}
	}



}