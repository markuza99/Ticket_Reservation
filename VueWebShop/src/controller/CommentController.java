package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Comment;
import beans.User;
import dto.ManifestationDTO;
import services.CommentService;

@Path("/comments")
public class CommentController {
	private CommentService commentService = new CommentService();
	
	@GET
	@Path("manifestation/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getCommentsForManifestation(@PathParam("id") String id) {
		return commentService.getCommentsForManifestation(id);
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> postComment(@Context HttpServletRequest request, Comment comment) {
		User u = (User) request.getSession().getAttribute("user");
		return commentService.postComment(u.getUsername(), comment);
	}

	@GET
	@Path("/comment-params/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ManifestationDTO getCommentParams(@Context HttpServletRequest request, @PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");
		return commentService.getCommentParams(user, id);
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getAllComments(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return commentService.getAllComments(user);
	}

	@PUT
	@Path("/approve-comment")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Comment> approveComment(@Context HttpServletRequest request, Comment comment) {
		User user = (User) request.getSession().getAttribute("user");
		return commentService.approveComment(user.getUsername(), comment);
	}
	
	@PUT
	@Path("/decline-comment")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Comment> declineComment(@Context HttpServletRequest request, Comment comment) {
		User user = (User) request.getSession().getAttribute("user");
		return commentService.declineComment(user.getUsername(), comment);
	}
}
