package com.example.studentrecords;

public class Data {

	private String id,name,email,courseCount;

	public Data(String id, String name, String email, String courseCount) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.courseCount = courseCount;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getCourseCount() {
		return courseCount;
	}

}
