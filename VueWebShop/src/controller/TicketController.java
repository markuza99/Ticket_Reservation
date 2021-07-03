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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Role;
import beans.Ticket;
import beans.User;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;
import dao.TicketDAO;
import dto.ReservationDTO;
import dto.SearchTicketsDTO;
import dto.TicketRepresentationDTO;
import services.TicketService;

@Path("/tickets")
public class TicketController {
	@Context
	ServletContext ctx;
	private TicketService ticketService;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("LocationDAO") == null) {
			LocationDAO locationDAO = new LocationDAO(contextPath);
			ctx.setAttribute(contextPath, locationDAO);
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			ManifestationDAO manifestationDAO = new ManifestationDAO(contextPath);
			ctx.setAttribute("ManifestationDAO", manifestationDAO);
		}
		if(ctx.getAttribute("TicketDAO") == null) {
			TicketDAO ticketDAO = new TicketDAO(contextPath);
			ctx.setAttribute("TicketDAO",ticketDAO);
			
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			CustomerDAO customerDAO = new CustomerDAO(contextPath);
			ctx.setAttribute("CustomerDAO", customerDAO);
		}
		if(ctx.getAttribute("SellerDAO") == null) {
			SellerDAO sellerDAO = new SellerDAO(contextPath);
			ctx.setAttribute("SellerDAO", sellerDAO);
		}
		ticketService = new TicketService((TicketDAO) ctx.getAttribute("TicketDAO"),
										(ManifestationDAO) ctx.getAttribute("ManifestationDAO"),
										(CustomerDAO) ctx.getAttribute("CustomerDAO"),
										(SellerDAO) ctx.getAttribute("SellerDAO"));
	}
	
	@GET
	@Path("/user-attended/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean userAttended(@Context HttpServletRequest request, @PathParam("id") String manifestationId) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return false;
		return ticketService.userAttended(user.getUsername(), manifestationId);
	} 
	
	@POST
	@Path("/reserve-ticket")
	@Consumes(MediaType.APPLICATION_JSON)
	public Ticket reserveTickets(@Context HttpServletRequest request, ReservationDTO reservationDTO) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return null;
		return ticketService.reserveTickets(reservationDTO, user.getUsername());
	}
	
	@PUT
	@Path("/delete/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteTicket(@PathParam("id") String ticketId) {
		ticketService.deleteTicket(ticketId);
	}
	
	@PUT
	@Path("/retrieve/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void retrieveTicket(@PathParam("id") String ticketId) {
		ticketService.retrieveTicket(ticketId);
	}
	
	@PUT
	@Path("/cancel-reservation/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Ticket reserveTickets(@Context HttpServletRequest request, @PathParam("id") String ticketId) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return null;
		if(user.getRole() != Role.CUSTOMER) return null;
		return ticketService.cancelReservation(ticketId, user.getUsername());
	}
	
	@GET
	@Path("/total-price")
	@Consumes(MediaType.APPLICATION_JSON)
	public double getTicketTotalPrice(@Context HttpServletRequest request, @QueryParam("numberOfTickets") int numberOfTickets,
	@QueryParam("ticketType") String ticketType, @QueryParam("manifestationId") String manifestationId) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return 0;
		return ticketService.getTicketTotalPrice(user.getUsername(), numberOfTickets, ticketType, manifestationId);
	}
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<TicketRepresentationDTO> getTickets(@Context HttpServletRequest request, @QueryParam("manifestationName") String manifestationName, 
			@QueryParam("priceFrom") double priceFrom, @QueryParam("priceTo") double priceTo, @QueryParam("dateFrom") String dateFrom,
			@QueryParam("dateTo") String dateTo, @QueryParam("sortBy") String sortBy, @QueryParam("ticketType") String ticketType,
			@QueryParam("ticketStatus") String ticketStatus) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return null;
		if(user.getRole() == Role.CUSTOMER) {
			return ticketService.getCustomerTickets(user.getUsername(), new SearchTicketsDTO(manifestationName, priceFrom, priceTo, dateFrom, dateTo, sortBy, ticketType, ticketStatus));
		} else if(user.getRole() == Role.SELLER) {
			return ticketService.getSellerTickets(user.getUsername(), new SearchTicketsDTO(manifestationName, priceFrom, priceTo, dateFrom, dateTo, sortBy, ticketType, ticketStatus));
		} else {
			return ticketService.getAllTickets(new SearchTicketsDTO(manifestationName, priceFrom, priceTo, dateFrom, dateTo, sortBy, ticketType, ticketStatus));
		}
	}
}
