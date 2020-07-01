package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

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

			super.onPostExecute(aVoid);
		}
	}


	private static final int RC_SIGN_IN = 1;
	private FirebaseAuth mAuth;
	private FirebaseUser user;
	EditText email,password;
	static String urlString;

	private final String TAG="LoginTag";

	SignInButton signInButton;
	GoogleSignInClient mGoogleSignInClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		mAuth = FirebaseAuth.getInstance();
		email = findViewById(R.id.editTextTextEmailAddress);
		password = findViewById(R.id.editTextTextPassword);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		 mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

		 signInButton = findViewById(R.id.signInGoogle);

		 signInButton.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 signIn();
			 }
		 });
	}

	public void onClick(View v){

		boolean flagE = false,flagP = false;
		String emailString,passwordString;
		emailString = email.getText().toString();
		passwordString = password.getText().toString();

		if(TextUtils.isEmpty(emailString)){
			flagE = true;
			email.setError("Empty field");
		}

		if(TextUtils.isEmpty(passwordString)){
			password.setError("Empty field");
			flagP = true;
		}

		if(flagE || flagP){
			return;
		}

		mAuth.signInWithEmailAndPassword(emailString, passwordString)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							Log.d(TAG, "signInWithEmail:success");
							startActivity(new Intent(MainActivity.this,Notes.class));

						} else {

							Log.w(TAG, "signInWithEmail:failure", task.getException());
							Toast.makeText(MainActivity.this, task.getException().getMessage(),
									Toast.LENGTH_SHORT).show();
						}

					}
				});
	}

	public void signUp(View v){

		startActivity(new Intent(MainActivity.this,SignUp.class));

		Log.d(TAG,"clickedOn Signup");
	}


	public void signIn() {
		Log.d("GoogleSignIn","Clicked");
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = task.getResult(ApiException.class);
				Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
				firebaseAuthWithGoogle(account.getIdToken());
			} catch (ApiException e) {
				// Google Sign In failed, update UI appropriately
				Log.w(TAG, "Google sign in failed", e);
				// ...
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		mAuth.getCurrentUser();
	}
	private void firebaseAuthWithGoogle(String idToken) {
		AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();

							startActivity(new Intent(MainActivity.this,Notes.class));


//							updateUI(user);
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithCredential:failure", task.getException());
//							updateUI(null);
						}

						// ...
					}
				});
	}

}