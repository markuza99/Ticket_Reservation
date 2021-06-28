package dto;

import beans.Gender;
import beans.Role;
import beans.User;

public class UserForViewDTO {
	public String firstName;
	public String lastName;
	public String username;
	public String gender;
	public String role;
	public int points;
	public String type;
	public boolean deleted;
	
	public UserForViewDTO(User user, int points, String type) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.username = user.getUsername();
		this.gender = getGender(user.getGender());
		this.role = getRole(user.getRole());
		this.points = points;
		this.type = type;
		this.deleted = user.getIsDeleted();
	}

	private String getRole(Role role) {
		if(role == Role.SELLER) return "prodavac";
		if(role == Role.CUSTOMER) return "kupac";
		return "admin";
	}

	private String getGender(Gender gender) {
		if(gender == Gender.FEMALE) return "zensko";
		return "musko";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
