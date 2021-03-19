package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import beans.Comment;
import beans.CommentApproval;
import beans.Manifestation;
import beans.Seller;
import beans.Ticket;
import dao.TicketDAO;
import dao.interfaces.ICommentDAO;
import dao.interfaces.IManifestationDAO;
import dao.interfaces.ISellerDAO;
import dao.interfaces.ITicketDAO;
import dto.CommentDTO;
import dto.CommentingConditionsDTO;

public class CommentService {
	private ICommentDAO commentDAO;
	private ITicketDAO ticketDAO;
	private IManifestationDAO manifestationDAO;
	private ISellerDAO sellerDAO;
	
	public CommentService(ICommentDAO commentDAO, TicketDAO ticketDAO, IManifestationDAO manifestationDAO, ISellerDAO sellerDAO) {
		this.commentDAO = commentDAO;
		this.ticketDAO = ticketDAO;
		this.manifestationDAO = manifestationDAO;
		this.sellerDAO = sellerDAO;
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
				c.setApproval("ACCEPTED");
				return commentDAO.update(c);
			}
		}
		return null;
	}
	
	public Comment declineComment(String username, CommentDTO commentDTO) {
		for(Comment c : commentDAO.getAll()) {
			if(c.getUser().equals(commentDTO.getUser()) && c.getManifestation().equals(commentDTO.getManifestation())) {
				c.setApproval("DENIED");
				return commentDAO.update(c);
			}
		}
		return null;
	}

	public CommentingConditionsDTO getCommentingConditions(String username, String id) {
		Manifestation manifestation = manifestationDAO.read(id);
		CommentingConditionsDTO ccDTO = new CommentingConditionsDTO(false, false, null, 0);
		if(manifestation.getEndTime().isBefore(LocalDateTime.now())) {
			ccDTO.setManifestationPassed(true);
			
			for (Ticket t : ticketDAO.getAll()) {
				if(t.getUser() == username && t.getManifestationId() == id) {
					ccDTO.setUserAttended(true);
					break;
				}
			}
			
			Comment comment = commentDAO.read(username + id);
			if(comment != null) {
				ccDTO.setCommentApproval(comment.getApproval());
				ccDTO.setUserRatingForManifestation(comment.getRating());
			} 
			
		}
		return ccDTO;
	}
	
	public int getManifestationRatingFromComments(String manifestationId) {
		int sumOfRatings = 0;
		int n = 0;
		for(Comment comment : commentDAO.getAll()) {
			if(comment.getManifestation().equals(manifestationId)
					&& comment.getIsDeleted() == true && comment.getApproval() == CommentApproval.ACCEPTED) {
				sumOfRatings += comment.getRating();
				n++;
			}
		}
		if(n == 0) {
			return 0;
		}
		// TO DO -- srediti average rating
		return sumOfRatings / n;
	}

	public List<Comment> getCommentsForSeller(String username) {
		List<Comment> sellersComments = new ArrayList<Comment>();
		
		for(Comment comment : commentDAO.getAll()) {
			Seller seller = sellerDAO.read(username);
			for(String manifestation : seller.getManifestations()) {
				if(comment.getManifestation().equals(manifestation)) {
					sellersComments.add(comment);
				}
			}
		}

		return sellersComments;
	}
}
