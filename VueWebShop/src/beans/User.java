package beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
<<<<<<< Updated upstream
import java.time.format.DateTimeFormatter;
=======
import java.util.Date;
>>>>>>> Stashed changes

public class User {
	private String username;
	private String firstName;
	private String lastName;
	private String password;
<<<<<<< Updated upstream
	private Gender gender;
	private LocalDate birthDate;
	private Role role;
=======
	private String gender;
	private String birthDate;
	private String role;
>>>>>>> Stashed changes
	
public User() {
		
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

<<<<<<< Updated upstream
	public User(String username, String firstName, String lastName, String password, Gender gender,
			LocalDate birthDate, Role role) {
=======
	public User(String username, String firstName, String lastName, String password, String gender,
			String birthDate, String role) {
>>>>>>> Stashed changes
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.gender = gender;
		this.birthDate = birthDate;
		this.role = role;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

<<<<<<< Updated upstream
	public LocalDate getBirthDate() {
=======
	public String getBirthDate() {
>>>>>>> Stashed changes
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
<<<<<<< Updated upstream
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		this.birthDate = LocalDate.parse(birthDate, format);
=======
		this.birthDate = birthDate;
>>>>>>> Stashed changes
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	@Override
	public String toString() {
		return "User [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", password="
				+ password + ", gender=" + gender + ", birthDate=" + birthDate + ", role=" + role + "]";
	}
}
