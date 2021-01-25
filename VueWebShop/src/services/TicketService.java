package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Ticket;
import beans.User;
import dao.TicketDAO;

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
}
