package controller;

import java.io.IOException;
import java.text.ParseException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import beans.User;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.interfaces.ISellerDAO;
import dto.ManifestationDTO;
import services.ManifestationService;

@Path("/manifestations")
public class ManifestationController {
	@Context
	ServletContext ctx;
	private ManifestationService manifestationService;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath));
		}
		manifestationService = new ManifestationService((ManifestationDAO) ctx.getAttribute("ManifestationDAO"),
														(LocationDAO) ctx.getAttribute("LocationDAO"),
														(ISellerDAO) ctx.getAttribute("SellerDAO"));

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
	public List<Manifestation> searchManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected) throws ParseException {
		return manifestationService.searchManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
	}
	

	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> filterManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected, @QueryParam("type") String izborTipa, @QueryParam("not_sold_out") boolean nijeRasprodato) throws ParseException {
		return manifestationService.filterManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo, selected, izborTipa, nijeRasprodato);
	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Manifestation updateManifestation(Manifestation manifestation) {
		return manifestationService.updateManifestation(manifestation);
	}

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Manifestation addManifestation(@Context HttpServletRequest request, ManifestationDTO manifestationDTO) {
		User user = (User) request.getSession().getAttribute("user");
		try {
			return manifestationService.addManifestation(manifestationDTO, user.getUsername());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
