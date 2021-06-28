package dto;

import beans.Comment;

public class CommentForViewDTO {
	public String id;
	public String user;
	public String manifestation;
	public String description;
	public int rating;
	public String approval;
	public boolean isDeleted;
	
	public CommentForViewDTO(Comment comment, String manifestationName) {
		this.id = comment.getUser() + comment.getManifestation();
		this.user = comment.getUser();
		this.manifestation = manifestationName;
		this.description = comment.getDescription();
		this.rating = comment.getRating();
		this.approval = comment.getApproval().toString();
		this.isDeleted = comment.getIsDeleted();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
