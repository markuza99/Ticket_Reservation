package dto;

import java.time.LocalDate;

import beans.User;

public class UserDTO {
	public String firstName;
	public String lastName;
	public String password;
	public String date;
	public String gender;
	
	private String username;
	private String role;
	private boolean deleted;
	
	public UserDTO() {}

	public UserDTO(String firstName, String lastName, String password, String date, String gender, String username,
			String role, boolean deleted) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.date = date;
		this.gender = gender;
		this.username = username;
		this.role = role;
		this.deleted = deleted;
	}
	
	public UserDTO(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.password = user.getPassword();
		this.date = user.getBirthDate().toString();
		this.gender = user.getGender().toString();
		this.username = user.getUsername();
		this.role = user.getRole().toString();
		this.deleted = user.getIsDeleted();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	
}
