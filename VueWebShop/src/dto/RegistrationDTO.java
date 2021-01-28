package dto;

import beans.User;

public class RegistrationDTO {
	private User user;
	private String oldUsername;
	
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOldUsername() {
		return oldUsername;
	}

	public void setOldUsername(String oldUsername) {
		this.oldUsername = oldUsername;
	}

	public RegistrationDTO() {

	}

	public RegistrationDTO(User user, String oldUsername) {
		this.user = user;
		this.oldUsername = oldUsername;
	}
	
	
	
}
