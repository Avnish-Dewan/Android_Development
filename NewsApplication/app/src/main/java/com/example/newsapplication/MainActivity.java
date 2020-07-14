package com.example.newsapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	RecyclerView recyclerView;
	RecyclerView.Adapter adapter;
	LinearLayoutManager layoutManager;

	RequestQueue queue;
	ArrayList<SourcesData> list;
	private final String URL="https://newsapi.org/v2/sources?language=en&apiKey=0b9381a21efd4a7b96c482812cb90a9f";
	public static final String TAG = "NEWSTAG";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		queue = Singleton.getInstance(this).getRequestQueue();

		list = new ArrayList<>();


		final JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				try {
					JSONArray array = response.getJSONArray("sources");

					for(int i = 0;i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
					    list.add(new SourcesData(object.getString("id"),object.getString("name"),object.getString("description"),object.getString("url")));
					}
					configureRecyclerView();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG,error.toString());
			}
		});
		queue.add(request);
		recyclerView = findViewById(R.id.recyclerView);
		layoutManager = new LinearLayoutManager(this);

	}

	private void configureRecyclerView() {

		adapter = new SourceAdapter(this,list);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);

	}
}