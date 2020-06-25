package com.example.rockpaperscissors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Game extends AppCompatActivity {

	final FirebaseDatabase db = FirebaseDatabase.getInstance();
	DatabaseReference reference;
	String player;
	TextView tv,tv1;
	Button rock,paper,scissors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		tv = findViewById(R.id.textView);
		tv1 = findViewById(R.id.textView2);

		Intent intent = getIntent();

		player = intent.getStringExtra("Player");

		Toast.makeText(Game.this,"Logged in as "+player,Toast.LENGTH_SHORT).show();

		reference = db.getReference();

		if(player.equals("player1")){
			reference.child("player1").child("isLoggedIn").setValue(true);
		}else{
			reference.child("player2").child("isLoggedIn").setValue(true);
		}


		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				tv.setText((String)snapshot.child("player1").child("selected").getValue());
				tv1.setText((String)snapshot.child("player2").child("selected").getValue());
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});




	}

	public void onClick(View v){

		String str="";
		switch (v.getId()){
			case R.id.rock:
				str = "rock";
				break;
			case R.id.paper:
				str = "paper";
				break;
			case R.id.scissor:
				str = "scissor";
				break;
		}

		reference.child(player).child("selected").setValue(str);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}