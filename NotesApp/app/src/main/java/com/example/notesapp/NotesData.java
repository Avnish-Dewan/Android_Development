package com.example.notesapp;


import java.util.Date;

public class NotesData {

	private String title;
	private String description;
	private Date created;

	public NotesData(String title, String description, Date created) {
		this.title = title;
		this.description = description;
		this.created = created;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}


	public Date getCreated() {
		return created;
	}
}
