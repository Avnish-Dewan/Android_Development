package com.example.studentrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void add(View v){
		Intent intent = new Intent(MainActivity.this,AddData.class);

		startActivity(intent);
	}


	public void getAllData(View v){

		boolean flag = false;
		if(v.getId() == R.id.viewAll)
			flag = true;

		Intent intent = new Intent(MainActivity.this,ShowData.class);

		Bundle bundle = new Bundle();

		bundle.putBoolean("ViewAll",flag);
		intent.putExtras(bundle);

		startActivity(intent);
	}

	public void deleteAllData(View v){
		DatabaseHelper helper = new DatabaseHelper(this);

		if(helper.deleteAll()>0){
			Toast.makeText(MainActivity.this,"Sucessfully deleted all data!",Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(MainActivity.this,"No data available to delete",Toast.LENGTH_SHORT).show();
		}
	}

	public void update(View v){
		startActivity(new Intent(MainActivity.this,Update.class));
	}


}