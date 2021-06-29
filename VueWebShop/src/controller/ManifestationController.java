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
import beans.Role;
import beans.User;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;
import dao.TicketDAO;
import dto.ManifestationDTO;
import dto.ManifestationForGridViewDTO;
import dto.ManifestationForViewDTO;
import dto.ManifestationParamsDTO;
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
		if(ctx.getAttribute("SellerDAO") == null) {
			System.out.println(contextPath);
			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			System.out.println(contextPath);
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath));
		}
		if(ctx.getAttribute("TicketDAO") == null) {
			System.out.println(contextPath);
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
		manifestationService = new ManifestationService((ManifestationDAO) ctx.getAttribute("ManifestationDAO"),
			(LocationDAO) ctx.getAttribute("LocationDAO"),
			(SellerDAO) ctx.getAttribute("SellerDAO"),
			(CustomerDAO) ctx.getAttribute("CustomerDAO"),
			(TicketDAO) ctx.getAttribute("TicketDAO"));

	}
	
	@GET
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ManifestationForGridViewDTO> getActiveManifestations(@QueryParam("name") String name,
			@QueryParam("place") String place,
			@QueryParam("priceFrom") int priceFrom, 
			@QueryParam("priceTo") int priceTo, 
			@QueryParam("dateFrom") String dateFrom,
			@QueryParam("dateTo") String dateTo, 
			@QueryParam("sortBy") String sortBy, 
			@QueryParam("type") String type,
			@QueryParam("status") String status, 
			@QueryParam("ticketCondition") String ticketCondition) throws ParseException {
		return manifestationService.getActiveManifestations(
				new ManifestationParamsDTO(name, place, priceFrom, priceTo, dateFrom,
						dateTo, sortBy, type, status, ticketCondition)
				);
	}
	
	@GET
	@Path("/list-mine")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ManifestationForGridViewDTO> getMineManifestations(@Context HttpServletRequest request, @QueryParam("name") String name, @QueryParam("place") String place,
			@QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("dateFrom") String dateFrom,
			@QueryParam("dateTo") String dateTo, @QueryParam("sortBy") String sortBy, @QueryParam("type") String type,
			@QueryParam("status") String status, @QueryParam("ticketCondition") String ticketCondition) throws ParseException {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return null;
		if(user.getRole() == Role.ADMIN) {
			return manifestationService.listAllManifestations(new ManifestationParamsDTO(name, place, priceFrom, priceTo, dateFrom,
			dateTo, sortBy, type, status, ticketCondition));
		} else if(user.getRole() == Role.SELLER) {
			return manifestationService.listSellerManifestations(
				user.getUsername(),
				new ManifestationParamsDTO(name, place, priceFrom, priceTo, dateFrom,
				dateTo, sortBy, type, status, ticketCondition));
		} else {
			return manifestationService.listUserManifestations(user.getUsername(), 
				new ManifestationParamsDTO(name, place, priceFrom, priceTo, dateFrom,
				dateTo, sortBy, type, status, ticketCondition));
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ManifestationForViewDTO getManifestation(@PathParam("id") String id) {
		return manifestationService.getManifestation(id);
	}
	
	@PUT
	@Path("/approve/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void approveManifestation(@PathParam("id") String id) {
		manifestationService.approveManifestation(id);
	}
	
	@PUT
	@Path("/decline/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void declineManifestation(@PathParam("id") String id) {
		manifestationService.declineManifestation(id);
	}
	
	@PUT
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteManifestation(@PathParam("id") String id) {
		manifestationService.deleteManifestation(id);
	}
	
	@PUT
	@Path("/retrieve/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void retrieveManifestation(@PathParam("id") String id) {
		manifestationService.retrieveManifestation(id);
	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Manifestation updateManifestation(ManifestationForViewDTO manifestation) {
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
