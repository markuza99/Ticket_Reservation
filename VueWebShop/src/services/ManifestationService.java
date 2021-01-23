package services;

import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Comment;
import beans.Manifestation;
import beans.User;
import dao.CommentDAO;
import dao.ManifestationDAO;
import dao.UserDAO;

@Path("/manifestationservice")
public class ManifestationService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("ManifestationDAO") == null) {
			
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath));
		}
		if(ctx.getAttribute("UserDAO") == null) {
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
		if(ctx.getAttribute("CommentDAO") == null) {
			ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			UserDAO userDAO = (UserDAO) ctx.getAttribute("UserDAO");
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath, manifestationDao, userDAO));
		}
		
	}
	
	@GET
	@Path("/getall")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getManifestations() {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return  manifestationDao.getFirstNManifestations(10);
	}
	
	@GET
	@Path("/getonemanifestation/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation getOneManifestation(@PathParam("id") String id) {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return manifestationDao.getOneManifestation(id);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getSearchManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		
		return manifestationDao.search(name, dateFrom, dateTo, place, priceFrom, priceTo);

	}
	
	@GET
	@Path("/getcomments/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getCommentsForManifestation(@PathParam("id") String id) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		return commentDAO.getCommentsForManifestation(id);
	}
	
	@GET
	@Path("/postcomment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> postComment(@Context HttpServletRequest request, Comment comment) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("CommentDAO");
		User u = (User) request.getSession().getAttribute("user");
		comment.setUser(u.getUsername());
		return commentDAO.postComment(comment);
	}
}
