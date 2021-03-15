package beans;

public class Comment {
	private String user;
	private String manifestation;
	private String description;
	private int rating;
	private CommentApproval approval; //accepted, denied, notChecked
	private boolean isDeleted;
	
	public Comment(String user, String manifestation, String description, int rating, CommentApproval approval, boolean isDeleted) {
		this.user = user;
		this.manifestation = manifestation;
		this.description = description;
		this.rating = rating;
		this.approval = approval;
		this.isDeleted = isDeleted;
	}
	
	public Comment() {}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted.equals("1") ? true : false;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public CommentApproval getApproval() {
		return approval;
	}

	public void setApproval(String approval) {
		this.approval = CommentApproval.valueOf(approval);
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
