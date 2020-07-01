package com.example.notesapp;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageHelper {

	private static final FirebaseStorage storage = FirebaseStorage.getInstance();
	private static final StorageReference reference = storage.getReference("images/");

	public static void uploadPhoto(Uri uri,String userID){

		reference.child(userID).putFile(uri);

	}

}
