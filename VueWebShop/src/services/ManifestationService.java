package services;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import dao.CommentDAO;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.TicketDAO;
import dao.UserDAO;

@Path("/manifestationservice")
public class ManifestationService {
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
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath, locationDAO));
		}
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("CustomerDAO", 
			new CustomerDAO(contextPath, ticketDAO, manifestationDAO));
		}
		if(ctx.getAttribute("UserDAO") == null) {
			CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath, customerDAO));
		}
		if(ctx.getAttribute("CommentDAO") == null) {
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath));
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

	public List<Manifestation> getSearchedManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected) throws ParseException {

		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		
		return manifestationDao.search(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
	}
	

	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getFilterManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected, @QueryParam("izborTipa") String izborTipa, @QueryParam("nijeRasprodato") boolean nijeRasprodato) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return manifestationDao.filter(name, dateFrom, dateTo, place, priceFrom, priceTo, selected,izborTipa, nijeRasprodato);
	}

	@PUT
	@Path("/update-manifestation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean updateManifestation(Manifestation manifestation) {
		ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		if(manifestationDAO.update(manifestation)) {
			return true;
		}
		return false;
	}
	
//	@POST
//	@Path("/upload")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.TEXT_PLAIN)
//	public String upload(@FormParam("file") InputStream file, @FormParam("fileName") String fileName) throws IOException {
//		String contextPath = ctx.getRealPath("");
////		System.out.println(file);
////		System.out.println(fileName);
//		return null;
//	}
}
