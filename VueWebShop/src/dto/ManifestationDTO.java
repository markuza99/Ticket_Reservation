package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import beans.ManifestationType;

public class ManifestationDTO {
	private String id;
	private String name;
	private ManifestationType type;
	private int numberOfSeats;
	private LocalDateTime date;
	private int ticketPrice;
	private String location;
	private String imageName;
	private String image64base;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ManifestationType getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = ManifestationType.valueOf(type);
	}
	
	public int getNumberOfSeats() {
		return numberOfSeats;
	}
	
	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public void setDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.date = LocalDateTime.parse(date, formatter);
	}
	
	public int getTicketPrice() {
		return ticketPrice;
	}
	
	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getImage64base() {
		return image64base;
	}
	
	public void setImage64base(String image64base) {
		this.image64base = image64base;
	}
	
//	public Boolean commentSucces;
//	public int manifestationRating;
//	public Boolean userAttended;
//	public String user;
//	public Role role;
//	
//	public ManifestationDTO() {}
//	
//	public ManifestationDTO(Boolean commentSucces, int manifestationRating, Boolean userAttended, String user,
//			Role role) {
//		this.commentSucces = commentSucces;
//		this.manifestationRating = manifestationRating;
//		this.userAttended = userAttended;
//		this.user = user;
//		this.role = role;
//	}
//
//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
//
//	public Boolean getCommentSucces() {
//		return commentSucces;
//	}
//
//	public void setCommentSucces(Boolean commentSucces) {
//		this.commentSucces = commentSucces;
//	}
//
//	public int getManifestationRating() {
//		return manifestationRating;
//	}
//
//	public void setManifestationRating(int manifestationRating) {
//		this.manifestationRating = manifestationRating;
//	}
//
//	public Boolean getUserAttended() {
//		return userAttended;
//	}
//
//	public void setUserAttended(Boolean userAttended) {
//		this.userAttended = userAttended;
//	}
//
//	public String getUser() {
//		return user;
//	}
//
//	public void setUser(String user) {
//		this.user = user;
//	}
	
	
}
