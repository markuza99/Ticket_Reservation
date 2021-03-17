package dto;

public class CommentDTO {
	private String user;
	private String manifestation;
	private String description;
	private int rating;
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getManifestation() {
		return manifestation;
	}
	
	public void setManifestation(String manifestation) {
		this.manifestation = manifestation;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
