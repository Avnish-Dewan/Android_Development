package com.example.studentrecords;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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


	public void deleteData(View v){

		final DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

		View promptsView = layoutInflater.inflate(R.layout.delete_prompt,null);
		final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

		builder.setView(promptsView);
		final EditText editText = promptsView.findViewById(R.id.editTextDialogUserInput);

		builder.setCancelable(false)
				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					String ID = editText.getText().toString();

					if(TextUtils.isEmpty(ID)){

						Toast.makeText(MainActivity.this,"Empty Input",Toast.LENGTH_SHORT).show();
						dialog.cancel();
//						onClick(dialog, which);
//						return;
					}else{
						int res = helper.deleteData(ID);

						if(res>0){
							Toast.makeText(MainActivity.this,"Data deleted",Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(MainActivity.this,"No data exists with ID="+ID,Toast.LENGTH_SHORT).show();
						}
					}
				}})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

}