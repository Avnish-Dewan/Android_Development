package com.example.studentrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class Update extends AppCompatActivity {

	boolean flag = false;

	EditText editTextID,editTextName,editTextEmail,editTextCourses;
	TextView textViewEmail,textViewName,textViewCourses;
	Button find;

	private void initialize() {
		editTextCourses = findViewById(R.id.editTextCourse);
		editTextEmail = findViewById(R.id.editTextEmail);
		editTextID = findViewById(R.id.editTextID);
		editTextName = findViewById(R.id.editTextName);

		textViewEmail = findViewById(R.id.textViewEmail);
		textViewName = findViewById(R.id.textViewName);
		textViewCourses = findViewById(R.id.textViewCourses);

		find = findViewById(R.id.find);
	}


	private void setError(EditText editText,String error){
		editText.setError(error);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);

		initialize();

	}

	private void toggle(boolean enabled){

		int x = enabled? View.VISIBLE: View.INVISIBLE;


		if(enabled){
			find.setText("UPDATE");
		}else{
			find.setText("FIND");
		}

		editTextID.setEnabled(!enabled);
		editTextName.setEnabled(enabled);
		editTextEmail.setEnabled(enabled);
		editTextCourses.setEnabled(enabled);

		editTextName.setVisibility(x);
		editTextEmail.setVisibility(x);
		editTextCourses.setVisibility(x);

		textViewName.setVisibility(x);
		textViewCourses.setVisibility(x);
		textViewEmail.setVisibility(x);
	}

	public void findData(View v){
		DatabaseHelper helper = new DatabaseHelper(Update.this);


		String IDString = editTextID.getText().toString();

		if(TextUtils.isEmpty(IDString)){
			editTextID.setError("Cannot be Empty");
			return;
		}

		if(!flag){

			Cursor cursor = helper.getData(IDString);

			Log.d("Cursor",cursor.getCount()+"");
			if(cursor.getCount() == 0){
				Toast.makeText(Update.this,"No data found",Toast.LENGTH_SHORT).show();
				return;
			}

			while(cursor.moveToNext()){
				editTextName.setText(cursor.getString(1));
				editTextEmail.setText(cursor.getString(2));
				editTextCourses.setText(cursor.getString(3));
			}
		}else{

			boolean check = false;

			String nameString = editTextName.getText().toString();
			String emailString = editTextEmail.getText().toString();
			String courseString = editTextCourses.getText().toString();

			if(TextUtils.isEmpty(nameString)){
				check = true;
				setError(editTextName,"Cannot be empty");
			}

			if(TextUtils.isEmpty(emailString)){
				check = true;
				setError(editTextEmail,"Cannot be empty");
			}

			if(TextUtils.isEmpty(courseString)){
				check = true;
				setError(editTextCourses,"Cannot be empty");
			}

			if(!EmailValidator.getInstance().isValid(emailString)){
				check=true;
				setError(editTextEmail,"Not a valid email");
			}

			if(check){
				return;
			}
			boolean res = helper.updateData(IDString,nameString,emailString,courseString);
			if(res){
				Toast.makeText(Update.this,"Data Successfully Updated",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Update.this,"Error Try again after some time",Toast.LENGTH_SHORT).show();
			}

			editTextID.setText("");
		}

		flag =  !flag;

		toggle(flag);
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);

		assert inputManager != null;
		inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

	}

	public void backButton(View v){
		startActivity(new Intent(Update.this,MainActivity.class));
	}

}