package com.example.newsapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
	Context context;
	public ArrayList<NewsData> list;

	public static class NewsViewHolder extends RecyclerView.ViewHolder{
		TextView title;
		TextView author;
		TextView publishedAt;
		ImageView newsImage;
		TextView description;
		TextView readMore;

		public NewsViewHolder(@NonNull View itemView) {
			super(itemView);

			title = itemView.findViewById(R.id.newsTitle);
			publishedAt = itemView.findViewById(R.id.publishTime);
			author = itemView.findViewById(R.id.author);
			newsImage = itemView.findViewById(R.id.newsImage);
			description = itemView.findViewById(R.id.newsDescription);
			readMore = itemView.findViewById(R.id.readMore);
		}
	}

	public NewsAdapter(Context context, ArrayList<NewsData> list) {
		this.context = context;
		this.list = list;
	}

	@NonNull
	@Override
	public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,parent,false);

		return new NewsViewHolder(view);

	}

	@Override
	public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

		final NewsData newsData = list.get(position);
		holder.title.setText(newsData.getTitle());
		holder.description.setText(newsData.getContent());
		Glide.with(context).load(newsData.getPhotoURL()).into(holder.newsImage);
		holder.publishedAt.setText(newsData.getPublishedAt().replace("T"," ").replace("Z",""));

		String authorName = newsData.getAuthor();

		if(authorName == null || authorName.equalsIgnoreCase("null")){
			holder.author.setText(newsData.getID());
		}else{
			holder.author.setText(authorName);
		}

		holder.readMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String urlString = newsData.getUrl();
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


	}

	@Override
	public int getItemCount() {
		return list.size();
	}


}
