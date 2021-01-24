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
	}
	
	@GET
	@Path("/getcomments/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getCommentsForManifestation(@PathParam("id") String id) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		return commentDAO.getCommentsForManifestation(id, Status.ACTIVE);
	}
	
	@POST
	@Path("/postcomment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> postComment(@Context HttpServletRequest request, Comment comment) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		User u = (User) request.getSession().getAttribute("user");
		comment.setUser(u.getUsername());
		return commentDAO.postComment(comment);
	}
	
	@GET
	@Path("/usercommented/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment userCommented(@Context HttpServletRequest request, @PathParam("id") String manifestationId) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		User u = (User) request.getSession().getAttribute("user");
		if(u != null) {
			return commentDAO.getUserCommentForManifestation(manifestationId, u.getUsername());
		}
		return null;
	}
	
	@GET
	@Path("/userattended/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment userAttended(@Context HttpServletRequest request, @PathParam("id") String manifestationId) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		User u = (User) request.getSession().getAttribute("user");
		if(u != null) {
			return commentDAO.userAttended(manifestationId, u.getUsername());
		}
		return null;
	}
	
	@GET
	@Path("/manifestationrating/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public int manifestationRating(@PathParam("id") String manifestationId) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		return commentDAO.getManifestationRating(manifestationId);
	}
}
