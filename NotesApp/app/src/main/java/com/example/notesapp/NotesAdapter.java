package com.example.notesapp;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

	ArrayList<NotesData> mList;
	static int deleteCount;

	public static class NotesViewHolder extends RecyclerView.ViewHolder{

		public TextView pos;
		public TextView title;
		public TextView description;
		public TextView date;
		public Button edit,delete;

		public NotesViewHolder(@NonNull View itemView) {
			super(itemView);
			pos = itemView.findViewById(R.id.pos);
			title = itemView.findViewById(R.id.noteTitle);
			description = itemView.findViewById(R.id.noteDesc);
			date = itemView.findViewById(R.id.noteDate);
			edit = itemView.findViewById(R.id.editNote);
			delete = itemView.findViewById(R.id.deleteNote);
		}
	}

	public NotesAdapter(ArrayList<NotesData> mList) {
		this.mList = mList;
		deleteCount = 0;
	}

	@NonNull
	@Override
	public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);

		return new NotesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final NotesViewHolder holder, final int position) {
		final NotesData notesData = mList.get(position);

		holder.pos.setText(String.valueOf(position));

		holder.title.setText(notesData.getTitle());
		holder.description.setText(notesData.getDescription());
		holder.date.setText("Modified On : "+notesData.getCreated().toString());

		holder.edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater li = LayoutInflater.from(v.getContext());
				View promptsView = li.inflate(R.layout.prompts_view,null);

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

				builder.setView(promptsView);
				final EditText title,desc;
				title=promptsView.findViewById(R.id.promptTitle);
				title.setText(notesData.getTitle());
				desc = promptsView.findViewById(R.id.promptDesc);
				desc.setText(notesData.getDescription());
				builder.setCancelable(false)
						.setPositiveButton("Update", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								NotesData data = new NotesData(title.getText().toString(),desc.getText().toString(),new Date());
								holder.title.setText(data.getTitle());
								holder.description.setText(data.getDescription());
								holder.date.setText(data.getCreated().toString());
								mList.remove(position);
								mList.add(position,data);

							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
		});


		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					mList.remove(position);
					notifyItemRemoved(position);
					notifyItemRangeChanged(position,mList.size());
					Notes.changeData(mList.size());


			}
		});



	}

	@Override
	public int getItemCount() {
		return mList.size();
	}
}
