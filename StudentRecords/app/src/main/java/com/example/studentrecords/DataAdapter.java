package com.example.studentrecords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

	ArrayList<Data> list;

	public static class DataViewHolder extends RecyclerView.ViewHolder{

		public TextView textId,textName,textEmail,textCount;

		public DataViewHolder(@NonNull View itemView) {
			super(itemView);

			textId = itemView.findViewById(R.id.id);
			textName = itemView.findViewById(R.id.name);
			textCount = itemView.findViewById(R.id.courseCount);
			textEmail = itemView.findViewById(R.id.email);

		}
	}

	public DataAdapter(ArrayList<Data> list) {
			this.list = list;
	}

	@NonNull
	@Override
	public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_shown,parent,false);

		return new DataViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
		Data data = list.get(position);
		holder.textId.setText(data.getId());
		holder.textEmail.setText(data.getEmail());
		holder.textName.setText(data.getName());
		holder.textCount.setText(data.getCourseCount());
	}

	@Override
	public int getItemCount() {
		return list.size();
	}


}
