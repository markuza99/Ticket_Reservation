package controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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
import beans.Role;
import beans.User;
import dao.CommentDAO;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;
import dao.TicketDAO;
import dao.UserDAO;
import dto.CommentDTO;
import dto.CommentForViewDTO;
import dto.CommentingConditionsDTO;
import services.CommentService;

@Path("/comments")
public class CommentController {
	@Context
	ServletContext ctx;
	private CommentService commentService;
	
	@PostConstruct
	public void init() {
		
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath));
		}
		if(ctx.getAttribute("SellerDAO") == null) {
			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath));
		}
		if(ctx.getAttribute("CommentDAO") == null) {
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath));
		}
		
		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
		
		commentService = new CommentService(commentDAO, ticketDAO, manifestationDAO, sellerDAO);
	}
	
	@GET
	@Path("manifestation/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getActiveAcceptedCommentsForManifestation(@PathParam("id") String id) {
		return commentService.getActiveAcceptedCommentsForManifestation(id);
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Comment postComment(@Context HttpServletRequest request, CommentDTO commentDTO) {
		User u = (User) request.getSession().getAttribute("user");
		if(u == null) return null;
		return commentService.postComment(u.getUsername(), commentDTO);
	}

	@GET
	@Path("/commenting-conditions/manifestation/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public CommentingConditionsDTO getCommentParams(@Context HttpServletRequest request, @PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");
		if(user != null && user.getRole() == Role.CUSTOMER) {
			return commentService.getCommentingConditions(user.getUsername(), id);
		}
		return new CommentingConditionsDTO(false, null, commentService.getManifestationRatingFromComments(id));
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentForViewDTO> getCommentsForSeller(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return null;
		if(user.getRole() == Role.SELLER)
			return commentService.getCommentsForSeller(user.getUsername());
		else
			return commentService.getAllComments();
	}

	@PUT
	@Path("/approve/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void approveComment(@Context HttpServletRequest request, @PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return;
		commentService.approveComment(id);
	}
	
	@PUT
	@Path("/decline/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void declineComment(@Context HttpServletRequest request, @PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return;
		commentService.declineComment(id);
	}
}
