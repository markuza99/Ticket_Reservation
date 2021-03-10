package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import beans.Comment;
import beans.Role;
import beans.Status;
import beans.User;
import dao.CommentDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;
import dao.TicketDAO;
import dto.ManifestationDTO;

public class CommentService {
	private CommentDAO commentDAO;
	private TicketDAO ticketDAO;
	
	public CommentService(CommentDAO commentDAO, TicketDAO ticketDAO) {
		this.commentDAO = commentDAO;
		this.ticketDAO = ticketDAO;
	}
	
	public List<Comment> getCommentsForManifestation(String id) {
		return commentDAO.getCommentsForManifestation(id, Status.ACTIVE);
	}
	
	public List<Comment> postComment(String username, Comment comment) {
		comment.setUser(username);
		return commentDAO.postComment(comment);
	}

	public ManifestationDTO getCommentParams(User user,String id) {
		ManifestationDTO manifestationDTO = new ManifestationDTO();
		manifestationDTO.manifestationRating = commentDAO.getManifestationRating(id);
		if(user != null) {
			manifestationDTO.commentSucces = commentDAO.userCommentedManifestation(id, user.getUsername());
			manifestationDTO.userAttended = ticketDAO.userAttended(id, user.getUsername());
			manifestationDTO.user = user.getUsername();
			manifestationDTO.role = user.getRole();
		}
		return manifestationDTO;
	}
	
	public List<Comment> getAllComments(User user) {
		if(user != null && user.getRole() == Role.SELLER) {
			return commentDAO.getCommentsForSeller(user.getUsername());
		} else {
			return commentDAO.getAllComments();
		}
	}

	public List<Comment> approveComment(String username, Comment comment) {
		return commentDAO.approveComment(comment, username);
	}
	
	public List<Comment> declineComment(String username, Comment comment) {
		return commentDAO.declineComment(comment, username);
	}
}
