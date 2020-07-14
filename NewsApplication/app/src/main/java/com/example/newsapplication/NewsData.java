package com.example.newsapplication;

public class NewsData {
	private String ID;
	private String title;
	private String photoURL;
	private String content;
	private String url;
	private String author;
	private String publishedAt;

	public NewsData(String title, String photoURL, String content, String url,String author,String publishedAt) {
		this.title = title;
		this.photoURL = photoURL;
		this.content = content;
		this.url = url;
		this.author = author;
		this.publishedAt = publishedAt;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public String getTitle() {
		return title;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public String getUrl() {
		return url;
	}

}
