package dto;

import beans.CommentApproval;

public class CommentingConditionsDTO {	// ovo mi mozda i ne treba jer cu dobiti iz same manifestacije
	private boolean userAttended;
	private CommentApproval commentApproval;
	private int userRatingForManifestation;
	
	public CommentingConditionsDTO(boolean userAttended, CommentApproval commentApproval, int userRatingForManifestation) {
		this.userAttended = userAttended;
		this.commentApproval = commentApproval;
		this.userRatingForManifestation = userRatingForManifestation;
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

	public int getUserRatingForManifestation() {
		return userRatingForManifestation;
	}
	
	public void setUserRatingForManifestation(int userRatingForManifestation) {
		this.userRatingForManifestation = userRatingForManifestation;
	}
	
}
