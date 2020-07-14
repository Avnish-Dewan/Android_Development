package com.example.newsapplication;

public class SourcesData {
	private String ID;
	private String name;
	private String description;
	private String URL;

	public SourcesData(String ID,String name, String description, String URL) {
		this.ID = ID;
		this.name = name;
		this.description = description;
		this.URL = URL;
	}

	public String getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getURL() {
		return URL;
	}
}
