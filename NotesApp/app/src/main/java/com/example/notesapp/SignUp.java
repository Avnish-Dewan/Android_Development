package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

	private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
	Uri uri;
	EditText name,password,email,confirmPass;

	public static final int PICK_IMAGE = 1;
	Bitmap bitmap;

	private CircleImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		imageView = findViewById(R.id.profilePicture);
		email = findViewById(R.id.signUpEmail);
		password = findViewById(R.id.signUpPass);
		name = findViewById(R.id.editName);

		confirmPass = findViewById(R.id.confirmPass);
	}

	public void onSignUp(View v){

		boolean flag = false;

		final String nameString = name.getText().toString();
		String emailString = email.getText().toString();
		String passwordString = password.getText().toString();
		String confirm = confirmPass.getText().toString();

		if(TextUtils.isEmpty(nameString)){
			flag = true;
			name.setError("Field Empty");
		}

		if(TextUtils.isEmpty(emailString)){
			flag = true;
			email.setError("Field Empty");
		}

		if(TextUtils.isEmpty(passwordString)){
			flag  = true;
			password.setError("Field Empty");
		}

		if(!confirm.equals(passwordString)){
			confirmPass.setError("Password not same");
			flag =true;
		}

		if(flag){
			return;
		}


		mAuth.createUserWithEmailAndPassword(emailString, passwordString)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d("TAG", "createUserWithEmail:success");
							FirebaseUser user = mAuth.getCurrentUser();

							DatabaseReference db= FirebaseDatabase.getInstance().getReference();
							db.child(user.getUid()).child("notesCount").setValue(0);

							UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
									.setDisplayName(nameString)
									.build();

							user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									Log.d("TAG","successfully updated");
								}
							});
							StorageReference mRef = FirebaseStorage.getInstance().getReference();
							if(bitmap !=null && uri!=null){

								mRef.child("images/"+user.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
									@Override
									public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
										Toast.makeText(SignUp.this,"Uploaded",Toast.LENGTH_SHORT).show();
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										startActivity(new Intent(SignUp.this,MainActivity.class));

									}
								}).addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										Toast.makeText(SignUp.this,"Error!!",Toast.LENGTH_SHORT).show();
									}
								});

							}else{

								uri = Uri.parse("android.resource://com.example.notesapp/" + R.drawable.defaultprofile);


								mRef.child("images/"+user.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
									@Override
									public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
										Toast.makeText(SignUp.this,"Uploaded",Toast.LENGTH_SHORT).show();

										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										startActivity(new Intent(SignUp.this,MainActivity.class));

									}
								}).addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										Toast.makeText(SignUp.this,"Error!!",Toast.LENGTH_SHORT).show();
									}
								});
							}


						} else {
							// If sign in fails, display a message to the user.
							Log.w("TAG", "createUserWithEmail:failure", task.getException());
							Toast.makeText(SignUp.this, "Authentication failed." + task.getException().getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public void uploadImage(View v){
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");

		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");

		Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

		startActivityForResult(chooserIntent, PICK_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE) {
			Log.d("OnActivity", "onActivityResult: done");

			uri = data.getData();

			try{
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

				imageView.setImageBitmap(bitmap);
//				imageView.setLayoutParams(new ConstraintLayout.LayoutParams(100, 100));


			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}