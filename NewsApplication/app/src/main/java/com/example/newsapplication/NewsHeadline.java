package com.example.newsapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;

public class NewsHeadline extends AppCompatActivity {
	private ArrayList<NewsData> newsList;
	RecyclerView recyclerView;
	RecyclerView.Adapter adapter;
	RecyclerView.LayoutManager layoutManager;
	RequestQueue queue;
	private final String URL = "https://newsapi.org/v2/everything?sources=";
	private final String API = "&language=en&apiKey=0b9381a21efd4a7b96c482812cb90a9f";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_headline);

		Intent intent = getIntent();
		final String ID = intent.getStringExtra("id");



		queue = Singleton.getInstance(this).getRequestQueue();

		newsList = new ArrayList<>();

		final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + ID + API, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				try {
					JSONArray array = response.getJSONArray("articles");


					for(int i = 0;i<array.length();i++){
						JSONObject object = array.getJSONObject(i);

						NewsData newsData = new NewsData(object.getString("title"),
								object.getString("urlToImage"),
								object.getString("description"),
								object.getString("url"),
								object.getString("author"),
								object.getString("publishedAt"));

						newsData.setID(ID);

						if(!(newsData.getPhotoURL()==null
								|| newsData.getPhotoURL().equalsIgnoreCase("NULL")
								|| newsData.getContent().equalsIgnoreCase("NULL")
								|| newsData.getContent() == null )) {
							newsList.add(newsData);
						}

					}
					configureRecyclerView();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(MainActivity.TAG,error.toString());
			}
		});


		queue.add(request);

		recyclerView = findViewById(R.id.recyclerViewNews);
		layoutManager = new LinearLayoutManager(this);
	}


	private void configureRecyclerView() {

		if(newsList.size() == 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = LayoutInflater.from(this).inflate(R.layout.prompt,null);
			builder.setView(view);
			builder.setCancelable(false);

			builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});

			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}

		adapter = new NewsAdapter(this,newsList);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
	}
}