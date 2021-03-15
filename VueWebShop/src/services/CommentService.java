package services;

import java.util.ArrayList;
import java.util.List;

import beans.Comment;
import beans.CommentApproval;
import beans.Status;
import dao.CommentDAO;
import dao.TicketDAO;
import dto.CommentDTO;

public class CommentService {
	private CommentDAO commentDAO;
	private TicketDAO ticketDAO;
	
	public CommentService(CommentDAO commentDAO, TicketDAO ticketDAO) {
		this.commentDAO = commentDAO;
		this.ticketDAO = ticketDAO;
	}
	
	public List<Comment> getActiveAcceptedCommentsForManifestation(String id) {
		return getCommentsForManifestation(id, false, CommentApproval.ACCEPTED);
	}
	
	public List<Comment> getCommentsForManifestation(String id, boolean isDeleted, CommentApproval commentApproval) {
		List<Comment> manifestationComments = new ArrayList<Comment>();

		for(Comment c : commentDAO.getAll()) {
			if(c.getManifestation().equals(id) && c.getIsDeleted() == isDeleted && c.getApproval() == commentApproval) {
				manifestationComments.add(c);
			}
		}
		return manifestationComments;
	}
	
	public Comment postComment(String username, CommentDTO commentDTO) {
		return commentDAO.create(new Comment(commentDTO.getUser(), commentDTO.getManifestation(), commentDTO.getDescription(),
				commentDTO.getRating(), CommentApproval.NOT_CHECKED, false));
	}

	public Comment approveComment(String username, CommentDTO commentDTO) {
		for(Comment c : commentDAO.getAll()) {
			if(c.getUser().equals(commentDTO.getUser()) && c.getManifestation().equals(commentDTO.getManifestation())) {
//				c.setCommentStatus("ACTIVE");
				c.setApproval("ACCEPTED");
				return commentDAO.update(c);
			}
		}
		return null;
	}
	
	public Comment declineComment(String username, CommentDTO commentDTO) {
		for(Comment c : commentDAO.getAll()) {
			if(c.getUser().equals(commentDTO.getUser()) && c.getManifestation().equals(commentDTO.getManifestation())) {
//				c.setCommentStatus("ACTIVE");
				c.setApproval("DENIED");
				return commentDAO.update(c);
			}
		}
		return null;
	}
//	public ManifestationDTO getCommentParams(User user,String id) {
//		ManifestationDTO manifestationDTO = new ManifestationDTO();
//		manifestationDTO.manifestationRating = commentDAO.getManifestationRating(id);
//		if(user != null) {
//			manifestationDTO.commentSucces = commentDAO.userCommentedManifestation(id, user.getUsername());
//			manifestationDTO.userAttended = ticketDAO.userAttended(id, user.getUsername());
//			manifestationDTO.user = user.getUsername();
//			manifestationDTO.role = user.getRole();
//		}
//		return manifestationDTO;
//	}
	
//	public List<Comment> getAllComments(User user) {
//		if(user != null && user.getRole() == Role.SELLER) {
//			return commentDAO.getCommentsForSeller(user.getUsername());
//		} else {
//			return commentDAO.getAllComments();
//		}
//	}
//
//	public List<Comment> approveComment(String username, Comment comment) {
//		return commentDAO.approveComment(comment, username);
//	}
//	
//	public List<Comment> declineComment(String username, Comment comment) {
//		return commentDAO.declineComment(comment, username);
//	}
}
