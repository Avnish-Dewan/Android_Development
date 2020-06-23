package com.example.studentrecords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class ShowData extends AppCompatActivity {

	RecyclerView recyclerView;
	ArrayList<Data> list;
	RecyclerView.LayoutManager layoutManager;
	DataAdapter adapter;

	Boolean viewAll;
	EditText id;
	Button view;
	DatabaseHelper helper;
	private void getData(){

		Cursor cursor = helper.getAllData();

		while(cursor.moveToNext()){
			list.add(new Data(cursor.getString(0),cursor.getString(1),
					cursor.getString(2),cursor.getString(3)));
		}
		configureView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_data);

		Intent intent = getIntent();

		viewAll = intent.getBooleanExtra("ViewAll",true);

		id =findViewById(R.id.editTextID);
		view = findViewById(R.id.viewButton);

		helper = new DatabaseHelper(this);
		list = new ArrayList<>();
		recyclerView = findViewById(R.id.recyclerView);

		if(!viewAll){
			id.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
		}else{
			id.setVisibility(View.GONE);
			view.setVisibility(View.GONE);
			getData();

			ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
			recyclerView.setLayoutParams(params);
		}


	}

	private void configureView(){

		layoutManager = new LinearLayoutManager(ShowData.this);
		adapter = new DataAdapter(list);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter) ;
	}



	public void viewData(View v){

		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);

		assert inputManager != null;
		inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		list.clear();

		String idText = id.getText().toString();
		if(TextUtils.isEmpty(idText)){
			id.setError("Cannot be Empty");
		}

		Cursor cursor=helper.getData(idText);

		if(cursor.getCount() == 0){
			Toast.makeText(ShowData.this,"No data found",Toast.LENGTH_SHORT).show();
			return;
		}

		while(cursor.moveToNext()){
			list.add(new Data(cursor.getString(0),cursor.getString(1),
					cursor.getString(2),cursor.getString(3)));
		}
		configureView();

	}
}