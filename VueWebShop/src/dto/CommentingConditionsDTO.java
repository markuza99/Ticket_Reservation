package dto;

import beans.CommentApproval;

public class CommentingConditionsDTO {
	private boolean userAttended;
	private CommentApproval commentApproval;
	private int manifestationRating;
	
	public CommentingConditionsDTO(boolean userAttended, CommentApproval commentApproval, int manifestationRating) {
		this.userAttended = userAttended;
		this.commentApproval = commentApproval;
		this.manifestationRating = manifestationRating;
	}
	
	public boolean isUserAttended() {
		return userAttended;
	}
	
	public void setUserAttended(boolean userAttended) {
		this.userAttended = userAttended;
	}

	public CommentApproval getCommentApproval() {
		return commentApproval;
	}

	public void setCommentApproval(CommentApproval commentApproval) {
		this.commentApproval = commentApproval;
	}

	public int getManifestationRating() {
		return manifestationRating;
	}
	
	public void setManifestationRating(int manifestationRating) {
		this.manifestationRating = manifestationRating;
	}
	
}
