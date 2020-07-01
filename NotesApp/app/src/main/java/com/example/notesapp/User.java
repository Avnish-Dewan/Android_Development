package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;




public class User extends AppCompatActivity {

	private  class MyTask extends AsyncTask<Void, Void, Void> {
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
			imageView.setImageBitmap(bitmap);
			CircleImageView navImageView = findViewById(R.id.nav_image);
			navImageView.setImageBitmap(bitmap);

			TextView navTextView = findViewById(R.id.navText);
			navTextView.setText(user.getDisplayName());
			textView.setText(user.getDisplayName());
			name.setText(user.getDisplayName());



			super.onPostExecute(aVoid);
		}
	}
	EditText name,email,phone;

	String urlString;
	private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
	private  FirebaseUser user;
	private static boolean flag = true;
	private DrawerLayout dl;
	private ActionBarDrawerToggle t;
	private NavigationView view;
	TextView textView;
	CircleImageView imageView;
	private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
	FirebaseDatabase database = FirebaseDatabase.getInstance();
	DatabaseReference myRef = database.getReference();
	Button uploadButton;
	TextView verificationText;
	private Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		user = mAuth.getCurrentUser();
		verificationText = findViewById(R.id.verificationMailText);

		mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser myUser= firebaseAuth.getCurrentUser();

				if(myUser.isEmailVerified()){
					verificationText.setText("Email is verified");
					findViewById(R.id.sendVerificationMail).setVisibility(View.GONE);
				}
			}
		});

		if(user.isEmailVerified()){
			verificationText.setText("Email is verified");
		}

		name = findViewById(R.id.editTextTextPersonName);
		email = findViewById(R.id.editTextTextEmailAddress2);
		phone = findViewById(R.id.editTextPhone);


		name.setText(user.getDisplayName());
		email.setText(user.getEmail());

		uploadButton = findViewById(R.id.userUploadButton);
		imageView = findViewById(R.id.dp);

		textView = findViewById(R.id.textName);


		assert user != null;
		textView.setText(user.getDisplayName());



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
				new MyTask().execute();
				e.printStackTrace();
			}
		});

		dl = findViewById(R.id.drawerLayout);
		t = new ActionBarDrawerToggle(User.this,dl,R.string.open,R.string.close);

		dl.addDrawerListener(t);

		t.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



		view = findViewById(R.id.nv);

		view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				int id = item.getItemId();
				switch (id){
					case R.id.account:
						dl.closeDrawers();
						break;
					case R.id.notesMenu:
						startActivity(new Intent(User.this,Notes.class));
						finish();
						break;
					case R.id.logout:
						mAuth.signOut();
						startActivity(new Intent(User.this,MainActivity.class));
						finish();
						break;

				}
				return true;
			}
		});

		final TextView menuTextView = (TextView) view.getMenu().findItem(R.id.notesMenu).getActionView();


		myRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				phone.setText((String)snapshot.child(user.getUid()).child("phone").getValue());
				menuTextView.setText(String.valueOf(snapshot.child(user.getUid()).child("notesCount").getValue()));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		});

		toggle(false);

	}

	private void toggle(boolean enabled){
		name.setEnabled(enabled);
		email.setEnabled(enabled);
		phone.setEnabled(enabled);

		int isVis = enabled ? View.VISIBLE:View.INVISIBLE;

		uploadButton.setVisibility(isVis);


	}

	public void onUpdate(View v) throws InterruptedException {
		toggle(flag);

		if(!flag){
			String nameString = name.getText().toString();
			String emailString = email.getText().toString();
			String phoneNumber = phone.getText().toString();


			if(!emailString.equals(user.getEmail())){

				user.updateEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if(task.isSuccessful()){
							Toast.makeText(User.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d("TAG",e.getMessage());
						Toast.makeText(User.this, "Failed", Toast.LENGTH_SHORT).show();
					}
				});
			}


			myRef.child(user.getUid()).child("phone").setValue(phoneNumber);


			if(!nameString.equals(user.getDisplayName())){

				UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
						.setDisplayName(nameString)
						.build();

				user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						Log.d("TAG","Successfully Updated Name");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(User.this, "Failed", Toast.LENGTH_SHORT).show();
					}
				});
			}

			if(uri != null)
			{
				StorageReference mRef = FirebaseStorage.getInstance().getReference();

				mRef.child("images/" + user.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
						Toast.makeText(User.this, "Uploaded", Toast.LENGTH_SHORT).show();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(User.this, "Error!!", Toast.LENGTH_SHORT).show();
					}
				});
			}

			Thread.sleep(2000);

			finish();
			startActivity(getIntent());

		}

		flag = !flag;
	}

	public void uploadImage(View v){
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");

		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");

		Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

		startActivityForResult(chooserIntent, SignUp.PICK_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SignUp.PICK_IMAGE) {
			Log.d("OnActivity", "onActivityResult: done");

			uri = data.getData();
			Bitmap bitmap = null;
			try{
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
				imageView.setImageBitmap(bitmap);

				}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public void sendMail(View v){
		user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if(task.isSuccessful()){
					Toast.makeText(User.this, "Verification mail sent", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(User.this, "Failed to send verification mail", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		if(user.isEmailVerified()){
			textView.setText("Email is verified");
		}

	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		if(t.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}
}