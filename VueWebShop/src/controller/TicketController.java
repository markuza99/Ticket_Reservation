package controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import services.TicketService;

@Path("/tickets")
public class TicketController {
	private TicketService ticketServise = new TicketService();
	
	@GET
	@Path("/user-attended/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean userAttended(@Context HttpServletRequest request, @PathParam("id") String manifestationId) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return false;
		return ticketServise.userAttended(user.getUsername(), manifestationId);
	}
}
