package dto;

public class CommentDTO {
	public String commentUser;
	public String commentManifestation;
	
	public CommentDTO(String commentUser, String commentManifestation) {
		this.commentUser = commentUser;
		this.commentManifestation = commentManifestation;
	}
	
	public CommentDTO() {}

	public String getCommentUser() {
		return commentUser;
	}

	public void setCommentUser(String commentUser) {
		this.commentUser = commentUser;
	}

	public String getCommentManifestation() {
		return commentManifestation;
	}

	public void setCommentManifestation(String commentManifestation) {
		this.commentManifestation = commentManifestation;
	}
	
	
}
