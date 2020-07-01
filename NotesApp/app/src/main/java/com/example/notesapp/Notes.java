package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Long.parseLong;

public class Notes extends AppCompatActivity {

	private class MyTask extends AsyncTask<Void, Void, Void> {
		Bitmap bitmap;
		@Override
		protected Void doInBackground(Void... voids) {
			URL url;
			try {
				url = new URL(urlString);
				bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (IOException e){
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void aVoid) {
			CircleImageView navImageView = findViewById(R.id.nav_image);
			navImageView.setImageBitmap(bitmap);

			TextView navTextView = findViewById(R.id.navText);
			navTextView.setText(user.getDisplayName());


			super.onPostExecute(aVoid);
		}
	}

	private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
	private final FirebaseUser user = mAuth.getCurrentUser();
	CircleImageView imageView;
	private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
	final FirebaseDatabase database = FirebaseDatabase.getInstance();
	DatabaseReference myRef;
	ArrayList<NotesData> list;

	DrawerLayout drawerLayout1;
	private ActionBarDrawerToggle t1;
	private NavigationView view1;
	String urlString;

	RecyclerView recyclerView;
	RecyclerView.LayoutManager layoutManager;
	RecyclerView.Adapter adapter;

	static TextView menuTextView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);

		StorageReference reference = mStorageRef.child("images/"+user.getUid());

		Log.d("TAG",reference.getName());




		reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
			@Override
			public void onSuccess(Uri uri) {
//				String URL = uri.getPath();
				urlString = uri.toString();
				Log.d("TAG",uri.toString());
				new MyTask().execute();

			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				urlString = user.getPhotoUrl().toString();

				Log.d("TAG",e.getMessage());
				Log.d("TAG",urlString);
				new MyTask().execute();
				e.printStackTrace();
			}
		});


		myRef = database.getReference(user.getUid());




		drawerLayout1 = findViewById(R.id.notesView);
		t1 = new ActionBarDrawerToggle(Notes.this,drawerLayout1,R.string.open,R.string.close);

		drawerLayout1.addDrawerListener(t1);

		t1.syncState();

		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



		view1 = findViewById(R.id.nv1);



		view1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				int id = item.getItemId();
				switch (id){
					case R.id.account:
						startActivity(new Intent(Notes.this,User.class));
						finish();
						break;
					case R.id.notesMenu:
						drawerLayout1.closeDrawers();
						break;
					case R.id.logout:
						mAuth.signOut();
						startActivity(new Intent(Notes.this,MainActivity.class));
						break;

				}
				return true;
			}
		});

		menuTextView = (TextView) view1.getMenu().findItem(R.id.notesMenu).getActionView();

		 list = new ArrayList<>();
		recyclerView = findViewById(R.id.recylerView);
		layoutManager = new LinearLayoutManager(Notes.this);

		loadData();
	}

	public static void changeData(long listSize){

		menuTextView.setText(String.valueOf(listSize));

	}

	private void loadData(){

		myRef.child("notesCount").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				menuTextView.setText(String.valueOf(snapshot.getValue()));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});

		myRef.child("notes").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {


				for(DataSnapshot dataSnapshot:snapshot.getChildren()){
					String title = (String)dataSnapshot.child("title").getValue();
					String desc = (String)dataSnapshot.child("description").getValue();
					long data = Long.parseLong((String) Objects.requireNonNull(dataSnapshot.child("date").getValue()));

					Date date = new Date(data);

					Log.d("TAG",title+" "+desc+" "+data+" "+date.toString());
					list.add(new NotesData(title,desc,date));
				}

				configureRecyclerView();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void configureRecyclerView(){


		adapter = new NotesAdapter(list);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);


	}


	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		if(t1.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}


	public void onAddButton(View v){

		final LayoutInflater li = LayoutInflater.from(Notes.this);
		View promptsView = li.inflate(R.layout.prompts_view,null);

		AlertDialog.Builder builder = new AlertDialog.Builder(Notes.this);

		builder.setView(promptsView);
		final EditText title,desc;
		title=promptsView.findViewById(R.id.promptTitle);
		desc = promptsView.findViewById(R.id.promptDesc);

		builder.setCancelable(false)
				.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String description = desc.getText().toString();
						String titleString = title.getText().toString();
						list.add(new NotesData(titleString,description,new Date()));
						changeData(list.size());
						configureRecyclerView();
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();


	}

	@Override
	protected void onStop() {
		super.onStop();
		int i = 0;
		myRef.child("notes").removeValue();
		for(NotesData notesData: list){

			myRef.child("notes").child(String.valueOf(i)).child("title").setValue(notesData.getTitle());
			myRef.child("notes").child(String.valueOf(i)).child("description").setValue(notesData.getDescription());
			myRef.child("notes").child(String.valueOf(i)).child("date").setValue(String.valueOf(notesData.getCreated().getTime()));

			i++;
		}
		myRef.child("notesCount").setValue(list.size());

	}

}