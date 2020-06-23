package com.example.studentrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.validator.routines.EmailValidator;

public class AddData extends AppCompatActivity {

	DatabaseHelper helper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_data);

		helper = new DatabaseHelper(this);
	}

	public void addData(View v){
		EditText name,email,courses;

		name = findViewById(R.id.name);
		email = findViewById(R.id.email);
		courses = findViewById(R.id.courses);

		boolean flagName = false,flagEmail = false;

		String nameString = name.getText().toString();
		String emailString = email.getText().toString();
		String coursesString = courses.getText().toString();

		if(TextUtils.isEmpty(nameString)){
			name.setError("Name cannot be empty");
			flagName = true;
		}
		if(TextUtils.isEmpty(emailString)){
			email.setError("EMail cannot be empty");
			flagEmail = true;
		}
		if(!flagEmail){
			if(!EmailValidator.getInstance().isValid(emailString)){
				email.setError("Not a Valid Email Address");
				flagEmail = true;
			}
		}



		if(!flagEmail && !flagName){

			if(TextUtils.isEmpty(coursesString)){
				coursesString="0";
			}

			long res = helper.insertData(nameString,emailString,coursesString);

			Log.d("IdData",res+"");

			TextView textView = findViewById(R.id.textID);
			textView.setVisibility(View.VISIBLE);
			if(res!=-1){
				String successful = "Data Added successfully \n ID :- "+res;

				name.setVisibility(View.GONE);
				email.setVisibility(View.GONE);
				courses.setVisibility(View.GONE);
				Button btn = findViewById(R.id.add);
				btn.setVisibility(View.GONE);

				textView.setText(successful);

				Toast.makeText(AddData.this,"Successfully added data",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AddData.this,"Error! Try Again after some time",Toast.LENGTH_SHORT).show();
			}
		}
		else{
			return;
		}



		Button addNewData = findViewById(R.id.addData);
		addNewData.setVisibility(View.VISIBLE);

	}

	public void back(View v){
		startActivity(new Intent(AddData.this,MainActivity.class));
	}


	public void addNewData(View v){
		Intent intent = getIntent();
		finish();
		startActivity(intent);

	}
}