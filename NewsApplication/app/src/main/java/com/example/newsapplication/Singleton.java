package com.example.newsapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {

	private static Singleton instance;
	private RequestQueue queue;

	public Singleton(Context context) {
		queue = Volley.newRequestQueue(context.getApplicationContext());
	}

	public static synchronized Singleton getInstance(Context context){

		if (instance == null) {
			instance = new Singleton(context);
		}
		return instance;
	}

	public RequestQueue getRequestQueue(){
		return queue;
	}


}

