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
import beans.Status;
import beans.User;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.interfaces.ISellerDAO;
import dto.ManifestationDTO;
import dto.ManifestationWithLocationDTO;
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
	public List<ManifestationWithLocationDTO> getActiveManifestations() {
//		return manifestationService.getActiveManifestations();
		return manifestationService.getActiveManifestationsWithLocation();
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
	public List<ManifestationWithLocationDTO> searchManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo) throws ParseException {
		List<Manifestation> searchedManifestations = manifestationService.searchAllManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo);
		return manifestationService.convertToManifestationsWithLocationDTO(searchedManifestations);
	}
	
	@GET
	@Path("/sort")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ManifestationWithLocationDTO> sortManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("place") String place, @QueryParam("sortBy") String sortBy) {
		List<Manifestation> searchedManifestations = manifestationService.searchAllManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo);
		List<Manifestation> sortedManifestations = manifestationService.sortManifestations(searchedManifestations, sortBy);
		return manifestationService.convertToManifestationsWithLocationDTO(sortedManifestations);
	}
	
	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ManifestationWithLocationDTO> filterManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("place") String place, @QueryParam("sortBy") String sortBy, @QueryParam("manifestationType") String manifestationType, @QueryParam("ticketCondition") String ticketCondition) throws ParseException {
		List<Manifestation> searchedManifestations = manifestationService.searchAllManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo);
		List<Manifestation> sortedManifestations = manifestationService.sortGivenManifestations(searchedManifestations, sortBy);
		List<Manifestation> filteredManifestations = manifestationService.filterManifestations(sortedManifestations, manifestationType, ticketCondition);
		return manifestationService.convertToManifestationsWithLocationDTO(filteredManifestations);
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
	public ManifestationWithLocationDTO addManifestation(@Context HttpServletRequest request, ManifestationDTO manifestationDTO) {
		User user = (User) request.getSession().getAttribute("user");
		try {
			Manifestation manifestation = manifestationService.addManifestation(manifestationDTO, user.getUsername());
			if(manifestation == null) return null;
			return manifestationService.convertToManifestationWithLocationDTO(manifestation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
