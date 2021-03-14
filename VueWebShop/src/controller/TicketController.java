package controller;

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

import beans.Ticket;
import beans.User;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.TicketDAO;
import dto.ReservationDTO;
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
		ticketService = new TicketService((TicketDAO) ctx.getAttribute("TicketDAO"),
										(ManifestationDAO) ctx.getAttribute("ManifestationDAO"),
										(CustomerDAO) ctx.getAttribute("CustomerDAO"));
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
}
