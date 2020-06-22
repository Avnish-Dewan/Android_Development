package com.example.studentrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

			boolean res = helper.insertData(nameString,emailString,coursesString);

			if(res){
				Toast.makeText(AddData.this,"Successfully added data",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AddData.this,"Error! Try Again after some time",Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void back(View v){
		startActivity(new Intent(AddData.this,MainActivity.class));
	}
}