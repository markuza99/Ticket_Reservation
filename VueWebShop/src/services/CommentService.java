package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Comment;
import beans.Status;
import beans.User;
import dao.CommentDAO;
import dao.TicketDAO;
import dto.ManifestationDTO;

@Path("/commentservice")
public class CommentService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("CommentDAO") == null) {
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath));
		}
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
	}
	
	
	
	@GET
	@Path("/get-comments/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getCommentsForManifestation(@PathParam("id") String id) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		return commentDAO.getCommentsForManifestation(id, Status.ACTIVE);
	}
	
	
	@POST
	@Path("/post-comment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> postComment(@Context HttpServletRequest request, Comment comment) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		User u = (User) request.getSession().getAttribute("user");
		comment.setUser(u.getUsername());
		return commentDAO.postComment(comment);
	}

	@GET
	@Path("/get-comment-params/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ManifestationDTO getCommentParams(@Context HttpServletRequest request, @PathParam("id") String id) {
		ManifestationDTO manifestationDTO = new ManifestationDTO();
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
		User user = (User) request.getSession().getAttribute("user");
		if(user != null) {
			manifestationDTO.commentSucces = commentDAO.userCommentedManifestation(id, user.getUsername());
			manifestationDTO.manifestationRating = commentDAO.getManifestationRating(id);
			manifestationDTO.userAttended = ticketDAO.userAttended(id, user.getUsername());
			manifestationDTO.user = user.getUsername();
		}
		return manifestationDTO;
	}

}
