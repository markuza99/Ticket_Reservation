package beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private Gender gender;
	private LocalDate birthDate;
	private Role role;
	private boolean isDeleted;
	
	public User() {
		
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String username, String firstName, String lastName, String password, Gender gender,
			LocalDate birthDate, Role role, boolean isDeleted) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.gender = gender;
		this.birthDate = birthDate;
		this.role = role;
		this.isDeleted = isDeleted;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = Gender.valueOf(gender.toUpperCase());
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.birthDate = LocalDate.parse(birthDate, formatter);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = Role.valueOf(role);
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted.equals("1") ? true : false;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", password="
				+ password + ", gender=" + gender + ", birthDate=" + birthDate + ", role=" + role + "]";
	}
}
