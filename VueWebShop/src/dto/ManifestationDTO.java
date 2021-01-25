package dto;

public class ManifestationDTO {
	public Boolean commentSucces;
	public int manifestationRating;
	public Boolean userAttended;
	public String user;
	
	public ManifestationDTO() {}
	
	public ManifestationDTO(Boolean commentSucces, int manifestationRating, Boolean userAttended, String user) {
		this.commentSucces = commentSucces;
		this.manifestationRating = manifestationRating;
		this.userAttended = userAttended;
		this.user = user;
	}

	public Boolean getCommentSucces() {
		return commentSucces;
	}

	public void setCommentSucces(Boolean commentSucces) {
		this.commentSucces = commentSucces;
	}

	public int getManifestationRating() {
		return manifestationRating;
	}

	public void setManifestationRating(int manifestationRating) {
		this.manifestationRating = manifestationRating;
	}

	public Boolean getUserAttended() {
		return userAttended;
	}

	public void setUserAttended(Boolean userAttended) {
		this.userAttended = userAttended;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
}
