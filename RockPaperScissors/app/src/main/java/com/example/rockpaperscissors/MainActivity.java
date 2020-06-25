package com.example.rockpaperscissors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

	final FirebaseDatabase db = FirebaseDatabase.getInstance();

	DatabaseReference referenceP1,referenceP2;

	static boolean flag1,flag2;

	Button player1,player2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		flag1 = false;
		flag2 = false;

		player1 = findViewById(R.id.button);
		player2 = findViewById(R.id.button2);

		referenceP1 = db.getReference("player1");
		referenceP2 = db.getReference("player2");

		referenceP1.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Boolean isLoggedIn = (Boolean)snapshot.child("isLoggedIn").getValue();

				flag1 = isLoggedIn;

				if(isLoggedIn){
					player1.setEnabled(false);
				}
				check();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});

		referenceP2.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Boolean isLoggedIn = (Boolean) snapshot.child("isLoggedIn").getValue();

				Log.d("MyTAG",isLoggedIn+"");

				flag2 = isLoggedIn;

				if(isLoggedIn){
					player2.setEnabled(false);
				}
				check();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});

	}

	private void check(){

		Log.d("MyTag",flag1+""+flag2);

		if(flag1 && flag2){
			Toast.makeText(MainActivity.this,"Room not free",Toast.LENGTH_SHORT).show();
		}
	}

	public void onPlayer1Click(View v){
		Intent intent = new Intent(MainActivity.this,Game.class);

		intent.putExtra("Player","player1");

		startActivity(intent);
	}

	public void onPlayer2Click(View v){
		Intent intent = new Intent(MainActivity.this,Game.class);

		intent.putExtra("Player","player2");

		startActivity(intent);
	}
}