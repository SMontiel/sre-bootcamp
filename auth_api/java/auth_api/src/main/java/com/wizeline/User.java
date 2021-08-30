package com.wizeline;

public class User {
	public final String username;
	public final String password;
	public final String salt;
	public final String role;

	public User(String username, String password, String salt, String role) {
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.role = role;
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", role='" + role + '\'' +
				'}';
	}
}
