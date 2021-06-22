package dto;

public class CommentDTO {
	public String manifestation;
	public String description;
	public int rating;
	
	public CommentDTO(String manifestation, String description, int rating) {
		this.manifestation = manifestation;
		this.description = description;
		this.rating = rating;
	}
	
	public CommentDTO() {}
	
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
