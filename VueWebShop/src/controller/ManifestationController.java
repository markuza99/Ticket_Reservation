package controller;

import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import dao.SellerDAO;
import dao.TicketDAO;
import dao.UserDAO;
import services.ManifestationService;

@Path("/manifestations")
public class ManifestationController {
	@Context
	ServletContext ctx;
	private ManifestationService manifestationService;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		
		if(ctx.getAttribute("ManifestationDAO") == null) {
			LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("LocationDAO");
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath, locationDAO));
		}
		manifestationService = new ManifestationService((ManifestationDAO) ctx.getAttribute("ManifestationDAO"));
//		if(ctx.getAttribute("SellerDAO") == null) {
//			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
//			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath, manifestationDAO));
//		}
//		if(ctx.getAttribute("TicketDAO") == null) {
//			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
//		}
//		if(ctx.getAttribute("CustomerDAO") == null) {
//			TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
//			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
//			ctx.setAttribute("CustomerDAO", 
//			new CustomerDAO(contextPath, ticketDAO, manifestationDAO));
//		}
//		
//		if(ctx.getAttribute("UserDAO") == null) {
//			CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
//			SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
//			ctx.setAttribute("UserDAO", new UserDAO(contextPath, customerDAO, sellerDAO));
//		}
//		if(ctx.getAttribute("CommentDAO") == null) {
//			SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
//			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath, sellerDAO));
//		}
	}
	
	@GET
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getActiveManifestations() {
		return manifestationService.getActiveManifestations();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation getManifestation(@PathParam("id") String id) {
		return manifestationService.getManifestation(id);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getSearchedManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected) throws ParseException {
		return manifestationService.getSearchedManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
	}
	

	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getFilterManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected, @QueryParam("type") String izborTipa, @QueryParam("not_sold_out") boolean nijeRasprodato) throws ParseException {
		return manifestationService.getFilterManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo, selected, izborTipa, nijeRasprodato);
	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean updateManifestation(Manifestation manifestation) {
		return manifestationService.updateManifestation(manifestation);
	}
}
