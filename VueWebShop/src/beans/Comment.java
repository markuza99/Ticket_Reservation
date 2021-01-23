package beans;

public class Comment {
	private String user;
	private String manifestation;
	private String description;
	private int rating;
	
	public Comment(String user, String manifestation, String description, int rating) {
		this.user = user;
		this.manifestation = manifestation;
		this.description = description;
		this.rating = rating;
	}

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
