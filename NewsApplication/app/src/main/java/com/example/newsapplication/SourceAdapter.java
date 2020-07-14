package com.example.newsapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceViewHolder> {
	static Context context;
	ArrayList<SourcesData> list;

	public static class SourceViewHolder extends RecyclerView.ViewHolder{
		View v;
		TextView sourceName;
		TextView sourceDesc;
		TextView sourceURL;
		SourcesData data;
		public SourceViewHolder(@NonNull View itemView) {
			super(itemView);
			v = itemView;
			sourceName = itemView.findViewById(R.id.sourceName);
			sourceDesc = itemView.findViewById(R.id.SourceDescription);
			sourceURL = itemView.findViewById(R.id.sourceURL);
		}

		public void setOnClickListener(){
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String id = data.getID();

					Intent intent = new Intent(context,NewsHeadline.class);
					intent.putExtra("id",id);
					context.startActivity(intent);
				}
			});
		}
	}

	public SourceAdapter(Context context ,ArrayList<SourcesData> list) {
		SourceAdapter.context = context;
		this.list = list;
	}

	@NonNull
	@Override
	public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_layout,parent,false);
		return new SourceViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {
		final SourcesData sourcesData = list.get(position);

		holder.data = sourcesData;

		holder.sourceName.setText(sourcesData.getName());
		holder.sourceDesc.setText(sourcesData.getDescription());
		holder.sourceURL.setText(sourcesData.getURL());

		holder.sourceURL.setPaintFlags(holder.sourceURL.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		holder.sourceURL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String urlString = sourcesData.getURL();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setPackage("com.android.chrome");
				try {
					context.startActivity(intent);
				} catch (ActivityNotFoundException ex) {
					// Chrome browser presumably not installed so allow user to choose instead
					intent.setPackage(null);
					context.startActivity(intent);
				}
			}
		});
		holder.setOnClickListener();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

}
