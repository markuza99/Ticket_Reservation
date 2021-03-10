package controller;

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

import beans.User;
import dao.TicketDAO;
import services.TicketService;

@Path("/tickets")
public class TicketController {
	@Context
	ServletContext ctx;
	private TicketService ticketService;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("TicketDAO") == null) {
			TicketDAO ticketDAO = new TicketDAO(contextPath);
			ctx.setAttribute("TicketDAO",ticketDAO);
			
		}
		ticketService = new TicketService((TicketDAO) ctx.getAttribute("TicketDAO"));
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
}
