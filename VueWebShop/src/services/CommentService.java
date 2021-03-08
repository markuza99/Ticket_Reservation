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
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("LocationDAO");
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath, locationDAO));
		}
		if(ctx.getAttribute("SellerDAO") == null) {
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath, manifestationDAO));
		}
		if(ctx.getAttribute("CommentDAO") == null) {
			SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath, sellerDAO));
		}
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
	}
	
	public List<Comment> getCommentsForManifestation(String id) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		return commentDAO.getCommentsForManifestation(id, Status.ACTIVE);
	}
	
	public List<Comment> postComment(String username, Comment comment) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");	
		comment.setUser(username);
		return commentDAO.postComment(comment);
	}

	public ManifestationDTO getCommentParams(User user,String id) {
		ManifestationDTO manifestationDTO = new ManifestationDTO();
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
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
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		if(user != null && user.getRole() == Role.SELLER) {
			return commentDAO.getCommentsForSeller(user.getUsername());
		} else {
			return commentDAO.getAllComments();
		}
	}

	public List<Comment> approveComment(String username, Comment comment) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		return commentDAO.approveComment(comment, username);
	}
	
	public List<Comment> declineComment(String username, Comment comment) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		return commentDAO.declineComment(comment, username);
	}
}
