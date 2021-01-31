package services;

import java.text.ParseException;
import java.util.ArrayList;
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

import beans.Manifestation;
import beans.Ticket;
import beans.User;
import dao.ManifestationDAO;
import dao.TicketDAO;
import dao.UserDAO;

@Path("/ticketservice")
public class TicketService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
	}
	
	@GET
	@Path("/user-attended/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean userAttended(@Context HttpServletRequest request, @PathParam("id") String manifestationId) {
		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
		User u = (User) request.getSession().getAttribute("user");
		if(u != null) {
			return ticketDAO.userAttended(manifestationId, u.getUsername());
		}
		return false;
	}
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ticket> allTicketsUser(@Context HttpServletRequest request) {	
		TicketDAO ticketDao = (TicketDAO) ctx.getAttribute("TicketDAO");
		User u = (User) request.getSession().getAttribute("user");
		if(u != null) {
			return ticketDao.getAllUserTickets(u.getUsername());
		}
		return null;
	}
	@GET
	@Path("/getall")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ticket> allTickets(@Context HttpServletRequest request) {	
		TicketDAO ticketDao = (TicketDAO) ctx.getAttribute("TicketDAO");
		User u = (User) request.getSession().getAttribute("user");
		if(u != null) {
			if(u.getRole().toString().equals("CUSTOMER")) {
				return ticketDao.getAllUserTickets(u.getUsername());
			} else if(u.getRole().toString().equals("ADMIN")) {
				return ticketDao.getAllTickets();
			} else if(u.getRole().toString().equals("SELLER")) {
				return ticketDao.getAllReservedTickets();
			}
			
		}
		return null;
	}
	
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ticket> getSearchedTickets(@Context HttpServletRequest request, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("text") String text) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		List<Manifestation> manifs = manifestationDao.getAllManifestations();
		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
		User u = (User) request.getSession().getAttribute("user");
		List<Ticket> srcTickets = ticketDAO.search(u.getUsername(), priceFrom, priceTo, dateFrom, dateTo, manifs, text);
//		List<Ticket> nameTicket = new ArrayList<Ticket>();
//		for (Ticket ticket : srcTickets) {
//			for (Manifestation m : manifs) {
//				if(m.getId().equals(ticket.getManifestationId())) {
//					if(m.getName().toLowerCase().contains(text.toLowerCase())) {
//						nameTicket.add(ticket);
//					}
//				}
//			}
//		}
		return srcTickets;
	}
	
	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ticket> getFilterManifestations(@Context HttpServletRequest request, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("text") String text, @QueryParam("ticket_type") String ticket_type, @QueryParam("ticket_status") String ticket_status) throws ParseException {
		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		User u = (User) request.getSession().getAttribute("user");
		List<Manifestation> manifs = manifestationDao.getAllManifestations();
		List<Ticket> srcTicket = ticketDAO.search(u.getUsername(), priceFrom, priceTo, dateFrom, dateTo, manifs, text);
		
		return ticketDAO.filter(srcTicket, ticket_type, ticket_status);
	}
}
